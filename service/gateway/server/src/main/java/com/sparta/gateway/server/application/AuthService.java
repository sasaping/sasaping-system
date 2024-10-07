package com.sparta.gateway.server.application;

import com.sparta.auth.dto.jwt.JwtClaim;

public interface AuthService {

  JwtClaim verifyToken(String token);

}
