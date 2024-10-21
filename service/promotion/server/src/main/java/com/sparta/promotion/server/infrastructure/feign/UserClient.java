package com.sparta.promotion.server.infrastructure.feign;

import com.sparta.promotion.server.application.service.UserService;
import com.sparta.promotion.server.infrastructure.configuration.UserFeignConfig;
import com.sparta.user.user_dto.infrastructure.UserDto;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

@FeignClient(name = "user", configuration = UserFeignConfig.class)
public interface UserClient extends UserService {

  @GetMapping("/internal/users/user-id")
  UserDto getUserByUserId(@RequestParam(value = "userId") Long userId);


}
