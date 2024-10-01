package com.sparta.order.server.infrastructure.client;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

@FeignClient(name = "product")
public interface ProductClient {

  // TODO Product DTO 로 받아오기
  @GetMapping("/api/product/{userId}")
  String getUser(@PathVariable("userId") Long userId);


}
