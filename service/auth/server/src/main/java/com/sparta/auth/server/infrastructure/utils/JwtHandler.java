package com.sparta.auth.server.infrastructure.utils;

import com.sparta.auth.server.application.dto.JwtClaim;
import com.sparta.auth.server.infrastructure.properties.JwtProperties;
import io.jsonwebtoken.Jwts;
import java.nio.charset.StandardCharsets;
import java.util.Date;
import java.util.Map;
import javax.crypto.SecretKey;
import javax.crypto.spec.SecretKeySpec;

public class JwtHandler {

  public static final String USER_ID = "USER_ID";
  public static final String USER_NAME = "USER_NAME";
  public static final String USER_ROLE = "USER_ROLE";
  private static final long MILLI_SECOND = 1000L;

  private final SecretKey secretKey;
  private final JwtProperties jwtProperties;

  public JwtHandler(JwtProperties jwtProperties) {
    this.jwtProperties = jwtProperties;
    secretKey =
        new SecretKeySpec(
            jwtProperties.getSecretKey().getBytes(StandardCharsets.UTF_8),
            Jwts.SIG.HS256.key().build().getAlgorithm());
  }

  public String createToken(JwtClaim jwtClaim) {
    Map<String, Object> tokenClaims = this.createClaims(jwtClaim);
    Date now = new Date(System.currentTimeMillis());
    long accessTokenExpireIn = jwtProperties.getAccessTokenExpireIn();

    return Jwts.builder()
        .claims(tokenClaims)
        .issuedAt(now)
        .expiration(new Date(now.getTime() + accessTokenExpireIn * MILLI_SECOND))
        .signWith(secretKey)
        .compact();
  }

  private Map<String, Object> createClaims(JwtClaim jwtClaim) {
    return Map.of(
        USER_ID, jwtClaim.getUserId(),
        USER_NAME, jwtClaim.getUsername(),
        USER_ROLE, jwtClaim.getRole());
  }

}
