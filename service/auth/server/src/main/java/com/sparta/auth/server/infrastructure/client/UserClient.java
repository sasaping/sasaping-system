package com.sparta.auth.server.infrastructure.client;

import com.sparta.auth.server.application.service.UserService;
import com.sparta.user.dto.infrastructure.UserDto;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

@FeignClient(name = "user")
public interface UserClient extends UserService {

  @GetMapping("/internal/users")
  UserDto getUserByUsername(@RequestParam(value = "username") String username);

}
