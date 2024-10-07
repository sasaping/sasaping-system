package com.sparta.payment.infrastructure.client;

import com.sparta.slack_dto.infrastructure.MessageInternalDto;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

@FeignClient(name = "slack")
public interface MessageClient {

  @PostMapping("/internal/message")
  void sendMessage(@RequestBody MessageInternalDto.Create messageRequest);


}
