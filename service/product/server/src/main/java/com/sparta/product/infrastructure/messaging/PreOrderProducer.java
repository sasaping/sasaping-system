package com.sparta.product.infrastructure.messaging;

import dto.OrderDto.OrderCreateRequest;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.core.KafkaTemplate;

@RequiredArgsConstructor
@Slf4j(topic = "PreOrderProducer in Product server")
public class PreOrderProducer {
  private final KafkaTemplate<String, Object> kafkaTemplate;

  public void send(String topic, Long preOrderId, OrderCreateRequest request) {
    kafkaTemplate.send(topic, preOrderId.toString(), request);
    log.info("send preorderRequest of {} to order server", preOrderId);
  }
}
