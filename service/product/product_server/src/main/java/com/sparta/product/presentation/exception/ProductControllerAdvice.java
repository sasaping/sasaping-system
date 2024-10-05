package com.sparta.product.presentation.exception;

import com.sparta.common.domain.response.ApiResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.method.annotation.MethodArgumentTypeMismatchException;

@ControllerAdvice
@Slf4j
public class ProductControllerAdvice {
  @ExceptionHandler(ProductServerException.class)
  public ResponseEntity<?> businessExceptionHandler(ProductServerException e) {
    ProductErrorCode errorCode = e.getErrorCode();
    log.error("Error occurs in ProductServer : {}", e.getErrorCode());
    return ResponseEntity.status(errorCode.getStatus())
        .body(ApiResponse.error(errorCode.getStatus().name(), errorCode.getMessage()));
  }

  @ExceptionHandler(MethodArgumentTypeMismatchException.class)
  public ResponseEntity<?> typeMismatchExceptionHandler(MethodArgumentTypeMismatchException e) {
    log.error("Type mismatch error occurs in {}", e.toString());
    String errorMessage = String.format("Invalid value for %s: '%s'", e.getName(), e.getValue());
    return ResponseEntity.status(HttpStatus.BAD_REQUEST)
        .body(ApiResponse.error(HttpStatus.BAD_REQUEST.name(), errorMessage));
  }

  @ExceptionHandler(RuntimeException.class)
  public ResponseEntity<?> runtimeExceptionHandler(RuntimeException e) {
    log.error("Error occurs in ProductServer : {}", e.getMessage());
    return ResponseEntity.status(ProductErrorCode.INTERNAL_SERVER_ERROR.getStatus())
        .body(
            ApiResponse.error(
                ProductErrorCode.INTERNAL_SERVER_ERROR.getStatus().name(), e.getMessage()));
  }
}
