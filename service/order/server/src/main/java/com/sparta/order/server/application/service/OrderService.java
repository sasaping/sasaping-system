package com.sparta.order.server.application.service;

import com.sparta.order.server.domain.model.Order;
import com.sparta.order.server.domain.model.OrderProduct;
import com.sparta.order.server.domain.model.vo.OrderState;
import com.sparta.order.server.domain.repository.OrderProductRepository;
import com.sparta.order.server.domain.repository.OrderRepository;
import com.sparta.order.server.exception.OrderErrorCode;
import com.sparta.order.server.exception.OrderException;
import com.sparta.order.server.infrastructure.client.ProductClient;
import com.sparta.order.server.infrastructure.client.UserClient;
import com.sparta.user.user_dto.infrastructure.PointHistoryDto;
import com.sparta.user.user_dto.infrastructure.UserDto;
import java.math.BigDecimal;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class OrderService {

  private final OrderRepository orderRepository;
  private final UserClient userClient;
  private final ProductClient productClient;
  private final OrderProductRepository orderProductRepository;
  private static final String POINT_HISTORY_TYPE_REFUND = "환불";
  private static final String POINT_DESCRIPTION_ORDER_CANCEL = "주문 취소";

  @Transactional
  public Long cancelOrder(Long userId, Long orderId) {
    UserDto user = userClient.getUser(userId);
    Order order = orderRepository.findById(orderId).orElseThrow(
        () -> new OrderException(OrderErrorCode.ORDER_NOT_FOUND)
    );

    if (!order.getState().equals(OrderState.PENDING_PAYMENT)
        && !order.getState().equals(OrderState.COMPLETED)) {
      throw new OrderException(OrderErrorCode.CANNOT_CANCEL_WHILE_SHIPPING, orderId);
    }

    refundPoint(userId, orderId, order.getPointPrice());
    rollbackStock(order);

    // TODO 쿠폰 사용 롤백

    order.cancel();
    orderRepository.save(order);
    return orderId;
  }

  private void refundPoint(Long userId, Long orderId, BigDecimal pointPrice) {
    PointHistoryDto pointHistory = new PointHistoryDto(userId, orderId, pointPrice
        , POINT_HISTORY_TYPE_REFUND, POINT_DESCRIPTION_ORDER_CANCEL);
    userClient.createPointHistory(pointHistory);
  }

  private void rollbackStock(Order order) {
    List<OrderProduct> orderProducts = orderProductRepository.findByOrder(order);
    Map<String, Integer> orderProductQuantities = orderProducts.stream().collect(Collectors.toMap(
        orderProduct -> orderProduct.getProductId(), orderProduct -> orderProduct.getQuantity()));

    productClient.rollbackStock(orderProductQuantities);
  }

}
