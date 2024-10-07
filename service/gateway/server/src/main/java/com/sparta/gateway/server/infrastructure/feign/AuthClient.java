package com.sparta.gateway.server.infrastructure.feign;

import com.sparta.auth.dto.jwt.JwtClaim;
import com.sparta.gateway.server.application.AuthService;
import com.sparta.gateway.server.infrastructure.configuration.AuthFeignConfig;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestHeader;

@FeignClient(name = "auth", url = "http://localhost:19093/internal/auth", configuration = AuthFeignConfig.class)
public interface AuthClient extends AuthService {

  @GetMapping("/verify")
  JwtClaim verifyToken(@RequestHeader("Authorization") String token);

}
