package com.sparta.order.server.infrastructure.client;

import com.sparta.user.user_dto.infrastructure.UserDto;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

@FeignClient(name = "user")
public interface UserClient {

  @GetMapping("/internal/users/user-id")
  UserDto getUser(@RequestParam(value = "userId") Long userId);

}
