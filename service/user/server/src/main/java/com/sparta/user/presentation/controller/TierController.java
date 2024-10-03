package com.sparta.user.presentation.controller;

import com.sparta.common.domain.response.ApiResponse;
import com.sparta.user.application.service.TierService;
import com.sparta.user.presentation.request.TierRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RequiredArgsConstructor
@RequestMapping("/api/tier")
@RestController
public class TierController {

  private final TierService tierService;

  @PostMapping
  public ApiResponse<?> createTier(@RequestBody TierRequest.Create request) {
    tierService.createTier(request);
    return ApiResponse.created(null);
  }

}
