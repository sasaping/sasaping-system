package com.sparta.order.server.infrastructure.client;

import com.sparta.user.user_dto.infrastructure.UserInternalDto.UserOrderResponse;
import org.springframework.web.bind.annotation.PathVariable;

//@FeignClient(name = "user")
public interface UserClient {

  //@GetMapping("/api/users/{userId}")
  UserOrderResponse getUser(@PathVariable("userId") Long userId);

  //@PostMapping("/internal/users/point")
  void usePoint(int point);

  String getAddress(@PathVariable("addressId") Long addressId);

}
