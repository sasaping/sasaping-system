package com.sparta.auth.server.infrastructure.feign;

import com.sparta.auth.server.application.service.UserService;
import com.sparta.auth.server.infrastructure.configuration.UserFeignConfig;
import com.sparta.user.user_dto.infrastructure.UserDto;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

@FeignClient(name = "user", configuration = UserFeignConfig.class)
public interface UserClient extends UserService {

  @GetMapping("/internal/users")
  UserDto getUserByUsername(@RequestParam(value = "username") String username);

}
