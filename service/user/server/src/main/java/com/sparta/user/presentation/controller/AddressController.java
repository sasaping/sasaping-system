package com.sparta.user.presentation.controller;

import com.sparta.auth.auth_dto.jwt.JwtClaim;
import com.sparta.common.domain.response.ApiResponse;
import com.sparta.user.application.dto.AddressResponse;
import com.sparta.user.application.service.AddressService;
import com.sparta.user.presentation.request.AddressRequest;
import jakarta.validation.Valid;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
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

  @PreAuthorize("hasRole('ROLE_USER')")
  @GetMapping("/me")
  public ApiResponse<List<AddressResponse.Get>> getAddressByUserId(
      @AuthenticationPrincipal JwtClaim claim
  ) {
    return ApiResponse.ok(addressService.getAddressByUserId(claim.getUserId()));
  }

  @PreAuthorize("hasRole('ROLE_ADMIN') or hasRole('ROLE_MANAGER')")
  @GetMapping
  public ApiResponse<List<AddressResponse.Get>> getAddressList() {
    return ApiResponse.ok(addressService.getAddressList());

  }

  @PreAuthorize("hasRole('ROLE_USER') or hasRole('ROLE_ADMIN') or hasRole('ROLE_MANAGER')")
  @PatchMapping("/{addressId}")
  public ApiResponse<?> updateAddress(
      @PathVariable Long addressId,
      @RequestBody @Valid AddressRequest.Update request,
      @AuthenticationPrincipal JwtClaim claim
  ) {
    addressService.updateAddress(claim.getUserId(), addressId, request);
    return ApiResponse.ok();
  }

}
