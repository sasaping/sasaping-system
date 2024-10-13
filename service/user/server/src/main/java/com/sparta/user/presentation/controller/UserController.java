package com.sparta.user.presentation.controller;

import com.sparta.auth.auth_dto.jwt.JwtClaim;
import com.sparta.common.domain.response.ApiResponse;
import com.sparta.user.application.dto.UserResponse;
import com.sparta.user.application.service.UserService;
import com.sparta.user.presentation.request.UserRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.GetMapping;
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

  @PreAuthorize("hasRole('ROLE_USER')")
  @GetMapping("/me")
  public ApiResponse<UserResponse.Info> getMyPage(
      @AuthenticationPrincipal JwtClaim claim
  ) {
    return ApiResponse.ok(userService.getMyPage(claim.getUserId()));
  }

  @PreAuthorize("hasRole('ROLE_ADMIN')")
  @GetMapping("/hello")
  public String hello(@AuthenticationPrincipal JwtClaim claim) {
    System.out.println("claim.getUserId() = " + claim.getUserId());
    System.out.println("claim.getUsername() = " + claim.getUsername());
    System.out.println("claim.getRole() = " + claim.getRole());
    return "Hello World!";
  }

}
