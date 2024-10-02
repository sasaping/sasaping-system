package com.sparta.order.server.infrastructure.client;

import org.springframework.cloud.openfeign.FeignClient;

@FeignClient(name = "product")
public interface ProductClient {

  // TODO ProductIds로 Product DTO 받아오기
  

}
