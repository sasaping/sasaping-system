package com.sparta.user.presentation.controller;

import com.sparta.auth.auth_dto.jwt.JwtClaim;
import com.sparta.common.domain.response.ApiResponse;
import com.sparta.user.application.service.AddressService;
import com.sparta.user.presentation.request.AddressRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RequiredArgsConstructor
@RequestMapping("/api/address")
@RestController
public class AddressController {

  private final AddressService addressService;

  @PostMapping
  public ApiResponse<?> createAddress(
      @RequestBody AddressRequest.Create request,
      @AuthenticationPrincipal JwtClaim claim
  ) {
    addressService.createAddress(claim.getUserId(), request);
    return ApiResponse.ok();
  }

}
