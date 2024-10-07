package com.sparta.auth.server.presentation.controller;

import com.sparta.auth.auth_dto.jwt.JwtClaim;
import com.sparta.auth.server.application.service.AuthService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RequiredArgsConstructor
@RequestMapping("/internal/auth")
@RestController
public class AuthInternalController {

  private final AuthService authService;

  @GetMapping("/verify")
  public JwtClaim verifyToken(@RequestHeader("Authorization") String token) {
    return authService.verifyToken(token);
  }
}
