package com.sparta.user.presentation.controller;

import com.sparta.common.domain.response.ApiResponse;
import com.sparta.user.application.service.UserService;
import com.sparta.user.presentation.request.UserRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RequiredArgsConstructor
@RequestMapping("/api/users")
@RestController
public class UserController {

  private final UserService userService;

  @PostMapping("/sign-up")
  public ApiResponse<Void> createUser(@RequestBody UserRequest.Create request) {
    userService.createUser(request);
    return ApiResponse.created(null);
  }

}
