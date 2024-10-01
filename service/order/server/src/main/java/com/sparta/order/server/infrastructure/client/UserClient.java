package com.sparta.order.server.infrastructure.client;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

@FeignClient(name = "user")
public interface UserClient {

  // TODO User DTO 로 변경
  @GetMapping("/api/user/{userId}")
  String getUser(@PathVariable("userId") Long userId);

}
