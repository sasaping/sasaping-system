package com.sparta.product.infrastructure.messaging;

import dto.OrderCreateRequest;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.core.KafkaTemplate;

@RequiredArgsConstructor
@Slf4j(topic = "PreOrderProducer in Product server")
public class PreOrderProducer {
  private final KafkaTemplate<String, Object> kafkaTemplate;

  public void send(String topic, String userId, OrderCreateRequest request) {
    kafkaTemplate.send(topic, userId, request);
    log.info(
        "send preorderRequest of {} to order server",
        request.getOrderProductInfos().get(0).getProductId());
  }
}
