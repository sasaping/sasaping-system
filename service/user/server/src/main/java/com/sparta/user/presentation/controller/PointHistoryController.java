package com.sparta.user.presentation.controller;

import com.sparta.auth.auth_dto.jwt.JwtClaim;
import com.sparta.common.domain.response.ApiResponse;
import com.sparta.user.application.dto.PointResponse;
import com.sparta.user.application.service.PointHistoryService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RequiredArgsConstructor
@RequestMapping("/api/users/point")
@RestController
public class PointHistoryController {

  private final PointHistoryService pointHistoryService;

  @PreAuthorize("hasRole('ROLE_USER')")
  @GetMapping("/me")
  public ApiResponse<Page<PointResponse.Get>> getPointHistoryByUser(
      @AuthenticationPrincipal JwtClaim claim,
      Pageable pageable
  ) {
    return ApiResponse.ok(pointHistoryService.getPointHistoryByUserId(claim.getUserId(), pageable));
  }

  @PreAuthorize("hasAnyRole('ROLE_MANAGER', 'ROLE_ADMIN')")
  @GetMapping("/{userId}")
  public ApiResponse<Page<PointResponse.Get>> getPointHistoryByUserId(
      @PathVariable(name = "userId") Long userId,
      Pageable pageable
  ) {
    return ApiResponse.ok(pointHistoryService.getPointHistoryByUserId(userId, pageable));
  }

}
