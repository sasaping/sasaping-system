package com.sparta.order.server.application.service;

import com.sparta.order.server.domain.model.Order;
import com.sparta.order.server.domain.model.OrderProduct;
import com.sparta.order.server.domain.repository.OrderProductRepository;
import com.sparta.order.server.domain.repository.OrderRepository;
import com.sparta.order.server.exception.OrderErrorCode;
import com.sparta.order.server.exception.OrderException;
import com.sparta.order.server.infrastructure.client.PaymentClient;
import com.sparta.order.server.infrastructure.client.ProductClient;
import com.sparta.order.server.infrastructure.client.UserClient;
import com.sparta.payment_dto.infrastructure.PaymentInternalDto;
import com.sparta.product_dto.ProductDto;
import com.sparta.user.user_dto.infrastructure.AddressDto;
import com.sparta.user.user_dto.infrastructure.PointHistoryDto;
import com.sparta.user.user_dto.infrastructure.UserDto;
import dto.OrderCreateRequest;
import dto.OrderProductInfo;
import feign.FeignException.FeignClientException;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Slf4j(topic = "OrderService")
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class OrderCreateService {

  private static final String COUPON_TAG = "COUPON";
  private static final String POINT_HISTORY_TYPE_USE = "사용";
  private static final String POINT_DESCRIPTION_ORDER_PAYMENT = "주문 결제";

  private final UserClient userClient;
  private final CartService cartService;
  private final OrderProductRepository orderProductRepository;
  private final OrderRepository orderRepository;
  private final ProductClient productClient;
  private final PaymentClient paymentClient;
  private final OrderRollbackService orderRollbackService;

  @Transactional
  public Long createOrder(Long userId, OrderCreateRequest request) {
    Map<String, Integer> deductedProductsQuantities = new HashMap<>();
    List<Long> usedCoupons = new ArrayList<>(); // 사용 쿠폰 목록
    Long pointHistoryId = null; // 포인트 내역 ID

    try {
      UserDto user = userClient.getUser(userId); //사용자 조회

      List<String> productIds = createProductIdList(request);
      Map<String, Integer> productQuantities = createProductQuantityMap(request);
      // 배송지 조회 API, 사용자 배송지인지 확인
      AddressDto address = userClient.getAddress(request.getAddressId());
      validateAddress(address, userId);
      // 주문할 상품들 Product 리스트 조회 API
      List<ProductDto> products = productClient.getProductList(productIds);

      // 쿠폰 사용 가능 여부 조회
      validateUseCoupon(request, products);
      // 각 상품 재고 확인
      validateProductStock(products, productQuantities);
      // 재고 감소 API
      productClient.updateStock(productQuantities);
      deductedProductsQuantities = productQuantities;

      // 각 상품에 달린 사용자 쿠폰 API 통해 쿠폰 ID 얻어오기
      // 쿠폰 조회 API
      // 임시 쿠폰가 적용
      BigDecimal couponPrice = BigDecimal.ZERO;
      // 사용자 쿠폰 사용 API

      Order order = createUniqueOrder(userId, request, products, couponPrice, address);
      Long savedOrderId = orderRepository.save(order).getOrderId();

      // 포인트 유효성 검사 및 사용
      if (request.getPointPrice().compareTo(BigDecimal.ZERO) > 0) {
        PointHistoryDto pointUserRequest = new PointHistoryDto(userId, savedOrderId,
            request.getPointPrice(), POINT_HISTORY_TYPE_USE, POINT_DESCRIPTION_ORDER_PAYMENT);
        pointHistoryId = validateAndUsePoint(user.getPoint(), request.getPointPrice(),
            pointUserRequest);
      }

      Map<String, BigDecimal> productPrices = products.stream().collect(
          Collectors.toMap(
              product -> product.getProductId().toString(),
              ProductDto::getDiscountedPrice)
      );

      Map<String, String> productNames = products.stream().collect(
          Collectors.toMap(
              product -> product.getProductId().toString(),
              product -> product.getProductName()
          )
      );

      // 주문 상품 하나씩 생성 TODO couponDto
      request.getOrderProductInfos()
          .forEach(
              productInfo -> createAndSaveOrderProduct(productInfo, null,
                  productPrices.get(productInfo.getProductId()),
                  productNames.get(productInfo.getProductId()), order));

      PaymentInternalDto.Create payment = new PaymentInternalDto.Create(
          userId, savedOrderId, order.getOrderNo(), request.getUserEmail(),
          order.getTotalRealAmount().longValue());
      // 결제 API 호출
      paymentClient.payment(payment);

      // 장바구니에 상품 삭제
      cartService.orderCartProduct(userId, productQuantities);

      return savedOrderId;

    } catch (FeignClientException | OrderException e) {
      orderRollbackService.rollbackTransaction(
          deductedProductsQuantities,
          usedCoupons,
          pointHistoryId);
      throw e;
    }
  }

  private void validateAddress(AddressDto address, Long userId) {
    if (!address.getUserId().equals(userId)) {
      throw new OrderException(OrderErrorCode.ADDRESS_MISMATCH, address.getAddressId());
    }
  }

  private Order createUniqueOrder(Long userId, OrderCreateRequest request,
      List<ProductDto> products, BigDecimal couponPrice, AddressDto address) {
    Order order;
    int attempts = 0;
    final int maxAttempts = 10;

    do {
      order = Order.createOrder(userId, request, products, couponPrice, address);
      attempts++;
    } while (isDuplicateOrderNo(order.getOrderNo()) && attempts < maxAttempts);

    if (attempts >= maxAttempts) {
      throw new OrderException(OrderErrorCode.ORDER_NO_GENERATION_FAILED);
    }

    return order;
  }

  private boolean isDuplicateOrderNo(String orderNo) {
    return orderRepository.existsByOrderNo(orderNo);
  }


  private void createAndSaveOrderProduct(OrderProductInfo productInfo, String couponDto,
      BigDecimal productPrice, String productName,
      Order order) {
    OrderProduct orderProduct = OrderProduct.createOrderProduct(
        productInfo.getProductId(),
        productPrice,
        productName,
        productInfo.getQuantity(),
        couponDto,
        order
    );
    orderProductRepository.save(orderProduct);
  }

  private Long validateAndUsePoint(BigDecimal userPoint, BigDecimal pointPrice,
      PointHistoryDto pointUserRequest) {
    if (userPoint.compareTo(pointPrice) < 0) {
      throw new OrderException(OrderErrorCode.INSUFFICIENT_POINT);
    }
    return userClient.createPointHistory(pointUserRequest);
  }

  private void validateProductStock(List<ProductDto> products,
      Map<String, Integer> productQuantities) {
    Map<String, ProductDto> productMap = products.stream()
        .collect(
            Collectors.toMap(product -> product.getProductId().toString(), product -> product));

    productQuantities.forEach((productId, quantity) -> {
      ProductDto product = productMap.get(productId);
      if (product == null || quantity > product.getStock()) {
        throw new OrderException(OrderErrorCode.INSUFFICIENT_STOCK, productId);
      }
    });
  }

  private void validateUseCoupon(OrderCreateRequest request, List<ProductDto> products) {
    List<String> usedCouponProductIds = request.getOrderProductInfos().stream()
        .filter(orderProductInfo -> orderProductInfo.getUserCouponId() != null)
        .map(OrderProductInfo::getProductId).toList();

    Map<String, List<String>> productTags = products.stream()
        .collect(Collectors.toMap(
            product -> product.getProductId().toString(), ProductDto::getTags
        ));

    usedCouponProductIds.forEach(usedCouponProductId -> {
      if (productTags.get(usedCouponProductId) == null || !productTags.get(usedCouponProductId)
          .contains(COUPON_TAG)) {
        throw new OrderException(OrderErrorCode.COUPON_NOT_APPLICABLE, usedCouponProductId);
      }
    });
  }

  private Map<String, Integer> createProductQuantityMap(OrderCreateRequest request) {
    return request.getOrderProductInfos().stream()
        .collect(Collectors.toMap(OrderProductInfo::getProductId, OrderProductInfo::getQuantity
        ));
  }

  private List<String> createProductIdList(OrderCreateRequest request) {
    return request.getOrderProductInfos().stream()
        .map(OrderProductInfo::getProductId).toList();
  }


}
