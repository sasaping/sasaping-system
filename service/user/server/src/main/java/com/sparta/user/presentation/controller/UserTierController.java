package com.sparta.user.presentation.controller;

import com.sparta.auth.auth_dto.jwt.JwtClaim;
import com.sparta.common.domain.response.ApiResponse;
import com.sparta.user.application.dto.UserTierResponse;
import com.sparta.user.application.service.UserService;
import com.sparta.user.presentation.request.UserRequest;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
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

  @PreAuthorize("hasAnyRole('ROLE_MANAGER', 'ROLE_ADMIN')")
  @GetMapping("/{userId}")
  public ApiResponse<UserTierResponse.Get> getUserTierByUserId(
      @PathVariable Long userId
  ) {
    return ApiResponse.ok(userService.getUserTierByUserId(userId));
  }

  @PreAuthorize("hasAnyRole('ROLE_MANAGER', 'ROLE_ADMIN')")
  @GetMapping
  public ApiResponse<Page<UserTierResponse.Get>> getUserTierList(Pageable pageable) {
    return ApiResponse.ok(userService.getUserTierList(pageable));
  }

  @PreAuthorize("hasAnyRole('ROLE_MANAGER', 'ROLE_ADMIN')")
  @PatchMapping("/{userId}")
  public ApiResponse<?> updateUserTier(
      @PathVariable(name = "userId") Long userId,
      @RequestBody @Valid UserRequest.UpdateTier request
  ) {
    userService.updateUserTier(userId, request);
    return ApiResponse.ok();
  }

}
