package com.sparta.order.server.application.service;

import com.sparta.order.server.domain.model.Order;
import com.sparta.order.server.domain.model.OrderState;
import com.sparta.order.server.domain.model.OrderType;
import com.sparta.order.server.infrastructure.client.UserClient;
import com.sparta.order.server.presentation.request.OrderCreateRequest;
import com.sparta.order.server.presentation.request.OrderProductInfo;
import exception.OrderErrorCode;
import exception.OrderException;
import java.util.List;
import java.util.UUID;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class OrderService {

  private final UserClient userClient;

  public void createOrder(Long userId, OrderCreateRequest request) {
    // 사용자 조회 API TODO UserDTO 객체로 변경
    String user = userClient.getUser(userId);

    // 배송지 조회 API
    Long addressId = 1L;

    // 사용자 포인트 유효성 검사
    int userPoint = 10000000;
    validateUserPoint(userPoint, request.getPointPrice());

    // 사용자 포인트 사용 API
    userClient.usePoint(request.getPointPrice());

    // 상품 ID 리스트 생성
    List<UUID> productIds = request.getOrderProductInfos().stream()
        .map(OrderProductInfo::getProductId).toList();

    // 주문할 상품들 Product 리스트 조회 API -> 재고 확인, 쿠폰 사용 가능 여부 확인

    // 각 상품에 달린 쿠폰 조회 API

    // 필요한 컬럼 totalQuantity, totalAmount, shippingAmount, totalRealAmount, pointPrice, couponPrice,

    // 각 상품 재고 확인하여 재고 감소 API

    // 각 상품에 적용한 쿠폰 사용 API

    // 쿠폰 금액 계산
    int couponPrice = 0;

    // 상품 총 수량
    int totalQuantity = request.getOrderProductInfos().stream()
        .mapToInt((OrderProductInfo::getQuantity)).sum();

    // 총 금액
    int totalProductAmount = request.getOrderProductInfos().stream()
        .mapToInt(orderProductInfo -> orderProductInfo.getQuantity() * orderProductInfo.getPrice())
        .sum();

    // 배송비
    int shippingAmount = totalProductAmount >= 20000 ? 0 : 3000;

    // 결제 금액
    int totalAmount = totalProductAmount + shippingAmount;

    // 실제 결제 금액
    int totalRealAmount = totalAmount - request.getPointPrice() - couponPrice;

    // 주문 생성
    Order order = Order.builder()
        .userId(userId)
        .orderNo("orderNo.")
        .type(OrderType.valueOf(request.getOrderType()))
        .state(OrderState.COMPLETED)
        .totalQuantity(totalQuantity)
        .totalAmount(totalProductAmount + shippingAmount)
        .shippingAmount(shippingAmount)
        .totalRealAmount(totalRealAmount)
        .pointPrice(request.getPointPrice())
        .couponPrice(couponPrice)
        .build();

    // 주문 상품 생성
    // 결제 API
  }

  private void validateUserPoint(int userPoint, int pointPrice) {
    if (userPoint < pointPrice) {
      throw new OrderException(OrderErrorCode.INSUFFICIENT_POINT);
    }
  }

}
