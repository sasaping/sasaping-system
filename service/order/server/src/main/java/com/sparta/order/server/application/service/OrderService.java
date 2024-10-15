package com.sparta.order.server.application.service;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.sparta.order.server.domain.model.Order;
import com.sparta.order.server.domain.model.OrderProduct;
import com.sparta.order.server.domain.model.vo.OrderState;
import com.sparta.order.server.domain.repository.OrderProductRepository;
import com.sparta.order.server.domain.repository.OrderRepository;
import com.sparta.order.server.exception.OrderErrorCode;
import com.sparta.order.server.exception.OrderException;
import com.sparta.order.server.infrastructure.client.ProductClient;
import com.sparta.order.server.infrastructure.client.UserClient;
import com.sparta.order.server.infrastructure.event.PaymentCompletedEvent;
import com.sparta.user.user_dto.infrastructure.PointHistoryDto;
import com.sparta.user.user_dto.infrastructure.UserDto;
import java.math.BigDecimal;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Slf4j
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

  @KafkaListener(topics = "payment-completed-topic", groupId = "order-service-group")
  public void handlePaymentCompletedEvent(String event) {
    try {
      ObjectMapper objectMapper = new ObjectMapper();
      PaymentCompletedEvent paymentCompletedEvent = objectMapper.readValue(event,
          PaymentCompletedEvent.class);
      Boolean success = paymentCompletedEvent.getSuccess();
      if (success) {
        // TODO : 결제 성공시 주문 완료 처리 (주문 상태 변경, 재고 차감)
      } else {
        // TODO :: 결제 실패시 주문 취소 처리
      }

    } catch (Exception e) {
      log.error(e.getMessage());
      throw new OrderException(OrderErrorCode.EVENT_PROCESSING_FAILED);
    }
  }

}
