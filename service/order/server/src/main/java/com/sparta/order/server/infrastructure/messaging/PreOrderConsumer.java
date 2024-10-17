package com.sparta.order.server.infrastructure.messaging;

import com.sparta.common.domain.entity.KafkaTopicConstant;
import com.sparta.order.server.application.service.OrderCreateService;
import dto.OrderCreateRequest;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.messaging.handler.annotation.Header;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Transactional
@Slf4j(topic = "PreOrderConsumer")
public class PreOrderConsumer {
  public final OrderCreateService orderCreateService;

  @KafkaListener(topics = KafkaTopicConstant.PROCESS_PREORDER, groupId = "product")
  public void consume(
      @Payload OrderCreateRequest request,
      @Header(name = "kafka_receivedMessageKey") String userId) {
    log.info("preorder by {}", userId);
    orderCreateService.createOrder(Long.parseLong(userId), request);
    log.info("process preorder : {}", request.getOrderProductInfos().get(0).getProductId());
  }
}
