package com.sparta.gateway.server.infrastructure.exception;

import com.sparta.common.domain.response.ApiResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
@Slf4j
public class GatewayExceptionHandler {

  @ExceptionHandler(GatewayException.class)
  public ApiResponse<?> handleException(GatewayException e) {
    log.error(e.getMessage());
    return ApiResponse.error(e.getStatusName(), e.getMessage());
  }
}
