package com.sparta.user.presentation.controller;

import com.sparta.user.application.service.AddressService;
import com.sparta.user.user_dto.infrastructure.AddressDto;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RequiredArgsConstructor
@RequestMapping("/internal/address")
@RestController
public class AddressInternalController {

  private final AddressService addressService;

  @GetMapping("/{addressId}")
  public AddressDto getAddress(@PathVariable(name = "addressId") Long addressId) {
    return addressService.getAddress(addressId);
  }

}
