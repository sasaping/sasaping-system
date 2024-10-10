package com.sparta.product.infrastructure.filter;

import com.sparta.auth.auth_dto.jwt.JwtClaim;
import java.util.Collection;
import java.util.Collections;
import lombok.Builder;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;

@Builder
public class JwtAuthentication implements Authentication {

  private Long userId;
  private String username;
  private String role;

  public static JwtAuthentication create(JwtClaim claims) {
    return JwtAuthentication.builder()
        .userId(claims.getUserId())
        .username(claims.getUsername())
        .role(claims.getRole())
        .build();
  }

  @Override
  public Collection<? extends GrantedAuthority> getAuthorities() {
    return Collections.singleton(new SimpleGrantedAuthority(role));
  }

  @Override
  public Object getCredentials() {
    return null;
  }

  @Override
  public Object getDetails() {
    return null;
  }

  @Override
  public Object getPrincipal() {
    return JwtClaim.create(userId, username, role);
  }

  @Override
  public boolean isAuthenticated() {
    return true;
  }

  @Override
  public void setAuthenticated(boolean isAuthenticated) throws IllegalArgumentException {}

  @Override
  public String getName() {
    return username;
  }
}
