package com.sparta.auth.server.application.service;

import static com.sparta.auth.server.domain.JwtConstant.MILLI_SECOND;
import static com.sparta.auth.server.domain.JwtConstant.USER_ID;
import static com.sparta.auth.server.domain.JwtConstant.USER_NAME;
import static com.sparta.auth.server.domain.JwtConstant.USER_ROLE;

import com.sparta.auth.dto.jwt.JwtClaim;
import com.sparta.auth.server.application.dto.AuthResponse;
import com.sparta.auth.server.exception.AuthErrorCode;
import com.sparta.auth.server.exception.AuthException;
import com.sparta.auth.server.infrastructure.properties.JwtProperties;
import com.sparta.auth.server.presentation.request.AuthRequest;
import com.sparta.user.dto.infrastructure.UserDto;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.JwtException;
import io.jsonwebtoken.Jwts;
import java.nio.charset.StandardCharsets;
import java.util.Date;
import java.util.Map;
import javax.crypto.SecretKey;
import javax.crypto.spec.SecretKeySpec;
import org.springframework.stereotype.Service;

@Service
public class AuthService {

  private final UserService userService;
  private final JwtProperties jwtProperties;
  private final SecretKey secretKey;

  public AuthService(UserService userService, JwtProperties jwtProperties) {
    this.userService = userService;
    this.jwtProperties = jwtProperties;
    this.secretKey = createSecretKey();
  }

  public AuthResponse.SignIn signIn(AuthRequest.SignIn request) {
    UserDto userData = userService.getUserByUsername(request.getUsername());

    // TODO(경민): 암호화 된 비밀번호로 비교해야함
    if (userData == null || !userData.getPassword().equals(request.getPassword())) {
      throw new AuthException(AuthErrorCode.SIGN_IN_FAIL);
    }

    return AuthResponse.SignIn.of(
        this.createToken(
            JwtClaim.create(
                userData.getUserId(),
                userData.getUsername(),
                userData.getRole()
            )
        )
    );
  }

  public JwtClaim verifyToken(String token) {
    try {
      Claims claims = Jwts.parser()
          .verifyWith(secretKey)
          .build()
          .parseSignedClaims(token)
          .getPayload();

      return this.convert(claims);
    } catch (ExpiredJwtException e) {
      throw new AuthException(AuthErrorCode.TOEKN_EXPIRED);
    } catch (JwtException e) {
      throw new AuthException(AuthErrorCode.INVALID_TOKEN);
    }
  }

  private SecretKey createSecretKey() {
    return new SecretKeySpec(
        jwtProperties.getSecretKey().getBytes(StandardCharsets.UTF_8),
        Jwts.SIG.HS256.key().build().getAlgorithm());
  }

  private String createToken(JwtClaim jwtClaim) {
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

  private JwtClaim convert(Claims claims) {
    return JwtClaim.create(
        claims.get(USER_ID, Long.class),
        claims.get(USER_NAME, String.class),
        claims.get(USER_ROLE, String.class));
  }

}
