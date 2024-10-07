package com.sparta.user.presentation.controller;

import com.sparta.common.domain.response.ApiResponse;
import com.sparta.user.application.dto.TierResponse;
import com.sparta.user.application.service.TierService;
import com.sparta.user.presentation.request.TierRequest;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RequiredArgsConstructor
@RequestMapping("/api/tier")
@RestController
public class TierController {

  private final TierService tierService;

  @PreAuthorize("hasRole('ROLE_ADMIN')")
  @PostMapping
  public ApiResponse<?> createTier(@RequestBody TierRequest.Create request) {
    tierService.createTier(request);
    return ApiResponse.created(null);
  }

  @PreAuthorize("hasRole('ROLE_ADMIN')")
  @GetMapping
  public ApiResponse<List<TierResponse.Get>> getTierList() {
    return ApiResponse.ok(tierService.getTierList());
  }

  @PreAuthorize("hasRole('ROLE_ADMIN')")
  @PatchMapping("/{tierId}")
  public ApiResponse<?> updateTier(
      @PathVariable(name = "tierId") Long tierId,
      @RequestBody TierRequest.Update request
  ) {
    tierService.updateTier(tierId, request);
    return ApiResponse.ok(null);
  }

}
