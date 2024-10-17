package com.sparta.product.infrastructure.filter;

import com.sparta.product.presentation.exception.ProductErrorCode;
import com.sparta.product.presentation.exception.ProductServerException;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.web.access.AccessDeniedHandler;
import org.springframework.stereotype.Component;

@Component
@Slf4j
public class CustomAccessDeniedHandler implements AccessDeniedHandler {
  @Override
  public void handle(
      HttpServletRequest request, HttpServletResponse response, AccessDeniedException ex)
      throws IOException, ServletException {
    log.info("CustomAccessDeniedHandler");
    throw new ProductServerException(ProductErrorCode.INVALID_PERMISSION);
  }
}
