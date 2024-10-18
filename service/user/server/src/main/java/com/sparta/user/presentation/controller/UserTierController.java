package com.sparta.user.presentation.controller;

import com.sparta.auth.auth_dto.jwt.JwtClaim;
import com.sparta.common.domain.response.ApiResponse;
import com.sparta.user.application.dto.UserTierResponse;
import com.sparta.user.application.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RequiredArgsConstructor
@RequestMapping("/api/users/tier")
@RestController
public class UserTierController {

  private final UserService userService;

  @PreAuthorize("hasRole('ROLE_USER')")
  @GetMapping("/me")
  public ApiResponse<UserTierResponse.Get> getUserTierByUser(
      @AuthenticationPrincipal JwtClaim claim
  ) {
    return ApiResponse.ok(userService.getUserTierByUserId(claim.getUserId()));
  }

}
