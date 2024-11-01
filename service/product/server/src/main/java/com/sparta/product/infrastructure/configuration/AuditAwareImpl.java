package com.sparta.product.infrastructure.configuration;

import java.util.Optional;
import org.springframework.data.domain.AuditorAware;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;

public class AuditAwareImpl implements AuditorAware<String> {

  @Override
  public Optional<String> getCurrentAuditor() {
    Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
    if (null == authentication || !authentication.isAuthenticated()) {
      return Optional.empty();
    }
    String username = authentication.getName();
    return Optional.of(username);
  }
}
