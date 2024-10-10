package com.sparta.order.server.infrastructure.client;

import com.sparta.user.user_dto.infrastructure.AddressDto;
import com.sparta.user.user_dto.infrastructure.PointHistoryDto;
import com.sparta.user.user_dto.infrastructure.UserDto;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;

@FeignClient(name = "user")
public interface UserClient {

  @GetMapping("/internal/users/user-id")
  UserDto getUser(@RequestParam(value = "userId") Long userId);

  @PostMapping("/internal/users/point")
  void usePoint(@RequestBody PointHistoryDto request);

  @GetMapping("/internal/address/{addressId}")
  AddressDto getAddress(@PathVariable(name = "addressId") Long addressId);

}
