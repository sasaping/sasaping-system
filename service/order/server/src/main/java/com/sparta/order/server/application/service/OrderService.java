package com.sparta.order.server.application.service;

import com.sparta.order.server.domain.model.Order;
import com.sparta.order.server.domain.model.OrderProduct;
import com.sparta.order.server.domain.repository.OrderProductRepository;
import com.sparta.order.server.domain.repository.OrderRepository;
import com.sparta.order.server.exception.OrderErrorCode;
import com.sparta.order.server.exception.OrderException;
import com.sparta.order.server.infrastructure.client.UserClient;
import com.sparta.order.server.presentation.dto.OrderDto.OrderCreateRequest;
import com.sparta.order.server.presentation.dto.OrderDto.OrderProductInfo;
import com.sparta.user.user_dto.infrastructure.UserInternalDto.UserOrderResponse;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class OrderService {

  private final UserClient userClient;
  private final CartService cartService;
  private final OrderProductRepository orderProductRepository;
  private final OrderRepository orderRepository;

  @Transactional
  public void createOrder(Long userId, OrderCreateRequest request) {
    // 사용자 조회 API
    UserOrderResponse user = userClient.getUser(userId);
    // 상품 ID 리스트 생성
    List<String> productIds = request.getOrderProductInfos().stream()
        .map(OrderProductInfo::getProductId).toList();
    // 해당 productId가 장바구니 Redis 에 존재하는지 확인
    //cartService.validateProductsInCart(userId, productIds);
    // 배송지 조회 API, 사용자 배송지인지 확인
    String address = userClient.getAddress(request.getAddressId());
    // 포인트 유효성 검사 및 사용
    validateAndUsePoint(user.getPoint(), request.getPointPrice());

    // 주문할 상품들 Product 리스트 조회 API -> 재고 확인, 쿠폰 사용 가능 여부 확인
    // 각 상품 재고 확인하여 재고 감소 API
    // 각 상품에 달린 사용자 쿠폰 API 통해 쿠폰 ID 얻어오기
    // 쿠폰 조회 API
    // 쿠폰가 적용
    int couponPrice = 0;
    // 사용자 쿠폰 사용 API

    Order order = createUniqueOrder(userId, request, couponPrice);
    orderRepository.save(order);

    // 주문 상품 하나씩 생성 TODO couponDto
    request.getOrderProductInfos()
        .forEach(productInfo -> createAndSaveOrderProduct(productInfo, null, order));

    // 결제 API 호출 ~~
  }

  private Order createUniqueOrder(Long userId, OrderCreateRequest request, int couponPrice) {
    Order order;
    int attempts = 0;
    final int maxAttempts = 10;

    do {
      order = Order.createOrder(userId, request, couponPrice);
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
      Order order) {
    OrderProduct orderProduct = OrderProduct.createOrderProduct(
        productInfo.getProductId(),
        productInfo.getQuantity(),
        couponDto,
        order
    );
    orderProductRepository.save(orderProduct);
  }

  private void validateAndUsePoint(int userPoint, int pointPrice) {
    if (userPoint < pointPrice) {
      throw new OrderException(OrderErrorCode.INSUFFICIENT_POINT);
    }
    userClient.usePoint(pointPrice);
  }

}
