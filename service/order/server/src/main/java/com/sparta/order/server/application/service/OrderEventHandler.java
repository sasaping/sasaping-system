package com.sparta.order.server.application.service;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.sparta.order.server.domain.model.Order;
import com.sparta.order.server.exception.OrderErrorCode;
import com.sparta.order.server.exception.OrderException;
import com.sparta.order.server.infrastructure.event.PaymentCompletedEvent;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@RequiredArgsConstructor
@Service
@Slf4j(topic = "OrderEventHandler")
public class OrderEventHandler {

  private final OrderService orderService;


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
        Order order = orderService.validateOrderExists(paymentCompletedEvent.getOrderId());
        order.complete();
        order.setPaymentId(paymentCompletedEvent.getPaymentId());
      } else {
        orderService.cancelOrder(paymentCompletedEvent.getUserId(),
            paymentCompletedEvent.getOrderId());
      }
    } catch (Exception e) {
      log.error(e.getMessage());
      throw new OrderException(OrderErrorCode.EVENT_PROCESSING_FAILED);
    }
  }

}
