package com.sparta.auth.server.exception;

import com.sparta.common.domain.response.ApiResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

@Slf4j
@ControllerAdvice
public class AuthControllerAdvice {

  @ExceptionHandler(AuthException.class)
  public ResponseEntity<?> businessExceptionHandler(AuthException e) {
    AuthErrorCode errorCode = e.getErrorCode();
    log.error("Error occurs in AuthServer : {}", e.getErrorCode());
    return ResponseEntity.status(errorCode.getStatus())
        .body(ApiResponse.error(errorCode.getStatus().name(), errorCode.getMessage()));
  }

  @ExceptionHandler(RuntimeException.class)
  public ResponseEntity<?> runtimeExceptionHandler(RuntimeException e) {
    log.error("Error occurs in AuthServer : {}", e.getMessage());
    return ResponseEntity.status(AuthErrorCode.INTERNAL_SERVER_ERROR.getStatus())
        .body(
            ApiResponse.error(
                AuthErrorCode.INTERNAL_SERVER_ERROR.getStatus().name(), e.getMessage()));
  }
}
