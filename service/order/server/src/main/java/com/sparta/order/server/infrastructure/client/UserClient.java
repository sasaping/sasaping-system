package com.sparta.order.server.infrastructure.client;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;

@FeignClient(name = "user")
public interface UserClient {

  // TODO User DTO 로 변경
  @GetMapping("/api/users/{userId}")
  String getUser(@PathVariable("userId") Long userId);

  // 포인트 사용 API 호출
  @PostMapping("/internal/users/point")
  void usePoint(int point);

}
