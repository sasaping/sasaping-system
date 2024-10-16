package com.sparta.order.server.application.service;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.sparta.order.server.domain.model.Order;
import com.sparta.order.server.domain.model.OrderProduct;
import com.sparta.order.server.domain.model.vo.OrderState;
import com.sparta.order.server.domain.repository.OrderProductRepository;
import com.sparta.order.server.domain.repository.OrderRepository;
import com.sparta.order.server.exception.OrderErrorCode;
import com.sparta.order.server.exception.OrderException;
import com.sparta.order.server.infrastructure.client.PaymentClient;
import com.sparta.order.server.infrastructure.client.ProductClient;
import com.sparta.order.server.infrastructure.client.UserClient;
import com.sparta.order.server.infrastructure.event.PaymentCompletedEvent;
import com.sparta.order.server.presentation.dto.OrderDto.OrderGetResponse;
import com.sparta.order.server.presentation.dto.OrderDto.OrderProductResponse;
import com.sparta.payment_dto.infrastructure.PaymentInternalDto;
import com.sparta.payment_dto.infrastructure.PaymentInternalDto.Cancel;
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
@Slf4j(topic = "OrderService")
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class OrderService {

  private final OrderRepository orderRepository;
  private final UserClient userClient;
  private final PaymentClient paymentClient;
  private final ProductClient productClient;
  private final OrderProductRepository orderProductRepository;
  private static final String POINT_HISTORY_TYPE_REFUND = "환불";
  private static final String POINT_DESCRIPTION_ORDER_CANCEL = "주문 취소";
  private static final String CANCEL_REASON = "단순 변심";

  @Transactional
  public Long cancelOrder(Long userId, Long orderId) {
    UserDto user = userClient.getUser(userId);
    Order order = validateOrderExists(orderId);
    order.validateOrderPermission(userId);
    order.validateOrderCancel();

    refundPoint(userId, orderId, order.getPointPrice());
    rollbackStock(order);

    if (order.getState().equals(OrderState.COMPLETED)) {
      cancelPayment(orderId);
    }

    // TODO 쿠폰 사용 롤백
    order.cancel();
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

  private void cancelPayment(Long orderId) {
    PaymentInternalDto.Cancel paymentCancel = new Cancel(orderId, CANCEL_REASON);
    paymentClient.cancelPayment(paymentCancel);
  }

  @Transactional
  @KafkaListener(topics = "payment-completed-topic", groupId = "order-service-group")
  public void handlePaymentCompletedEvent(String event) {
    try {
      ObjectMapper objectMapper = new ObjectMapper();
      PaymentCompletedEvent paymentCompletedEvent = objectMapper.readValue(event,
          PaymentCompletedEvent.class);
      Boolean success = paymentCompletedEvent.getSuccess();
      if (success) {
        log.info("===== 결제 성공 =====");
        // TODO : 재고 차감 로직
        Order order = validateOrderExists(paymentCompletedEvent.getOrderId());
        order.complete();
        order.setPaymentId(paymentCompletedEvent.getPaymentId());
      } else {
        cancelOrder(paymentCompletedEvent.getUserId(), paymentCompletedEvent.getOrderId());
      }

    } catch (Exception e) {
      log.error(e.getMessage());
      throw new OrderException(OrderErrorCode.EVENT_PROCESSING_FAILED);
    }
  }

  public OrderGetResponse getOrder(Long userId, Long orderId) {
    UserDto user = userClient.getUser(userId);
    Order order = validateOrderExists(orderId);

    order.validateOrderPermission(userId);

    final List<OrderProduct> orderProducts = orderProductRepository.findByOrder(order);
    final List<OrderProductResponse> orderProductResponses = orderProducts.stream().map(
        OrderProductResponse::fromEntity).toList();
    final PaymentInternalDto.Get payment = paymentClient.getPayment(orderId);

    return OrderGetResponse.from(order, orderProductResponses, payment);
  }

  private Order validateOrderExists(Long orderId) {
    return orderRepository.findById(orderId).orElseThrow(
        () -> new OrderException(OrderErrorCode.ORDER_NOT_FOUND)
    );
  }

}
