package com.sparta.user.presentation.controller;

import com.sparta.auth.auth_dto.jwt.JwtClaim;
import com.sparta.common.domain.response.ApiResponse;
import com.sparta.user.application.dto.PointResponse;
import com.sparta.user.application.service.PointHistoryService;
import com.sparta.user.application.dto.UserResponse;
import com.sparta.user.application.service.UserService;
import com.sparta.user.presentation.request.UserRequest;
import java.util.List;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RequiredArgsConstructor
@RequestMapping("/api/users")
@RestController
public class UserController {

  private final UserService userService;
  private final PointHistoryService pointHistoryService;

  @PostMapping("/sign-up")
  public ApiResponse<Void> createUser(@RequestBody UserRequest.Create request) {
    userService.createUser(request);
    return ApiResponse.created(null);
  }

  @PreAuthorize("hasRole('ROLE_USER')")
  @GetMapping("/point/me")
  public ApiResponse<List<PointResponse.Get>> getPointHistoryByUserid(
      @AuthenticationPrincipal JwtClaim claim
  ) {
    return ApiResponse.ok(pointHistoryService.getPointHistoryByUserid(claim.getUserId()));
  }

  @PreAuthorize("hasRole('ROLE_USER')")
  @GetMapping("/me")
  public ApiResponse<UserResponse.Info> getMyPage(
      @AuthenticationPrincipal JwtClaim claim
  ) {
    return ApiResponse.ok(userService.getUserById(claim.getUserId()));
  }

  @PreAuthorize("hasAnyRole('ROLE_MANAGER', 'ROLE_ADMIN')")
  @GetMapping("/{userId}")
  public ApiResponse<UserResponse.Info> getUser(@PathVariable(name = "userId") Long userId) {
    return ApiResponse.ok(userService.getUserById(userId));
  }

  @PreAuthorize("hasAnyRole('ROLE_MANAGER', 'ROLE_ADMIN')")
  @GetMapping
  public ApiResponse<List<UserResponse.Info>> getUserList() {
    return ApiResponse.ok(userService.getUserList());
  }

  @PreAuthorize("hasAnyRole('ROLE_USER', 'ROLE_MANAGER', 'ROLE_ADMIN')")
  @PatchMapping("/reset-password")
  public ApiResponse<?> updateUserPassword(
      @RequestBody @Valid UserRequest.UpdatePassword request,
      @AuthenticationPrincipal JwtClaim claim
  ) {
    userService.updateUserPassword(claim.getUserId(), request);
    return ApiResponse.ok();
  }

  @PreAuthorize("hasAnyRole('ROLE_MANAGER', 'ROLE_ADMIN')")
  @DeleteMapping("/{userId}")
  public ApiResponse<?> deleteUser(@PathVariable(name = "userId") Long userId) {
    userService.deleteUser(userId);
    return ApiResponse.ok();
  }


}
