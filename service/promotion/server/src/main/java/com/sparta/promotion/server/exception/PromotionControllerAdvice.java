package com.sparta.promotion.server.exception;

import com.sparta.common.domain.response.ApiResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

@Slf4j
@ControllerAdvice
public class PromotionControllerAdvice {

  @ExceptionHandler(PromotionException.class)
  public ResponseEntity<?> businessExceptionHandler(PromotionException e) {
    PromotionErrorCode errorCode = e.getErrorCode();
    log.error("Error occurs in PromotionServer : {}", e.getErrorCode());
    return ResponseEntity.status(errorCode.getStatus())
        .body(ApiResponse.error(errorCode.getStatus().name(), errorCode.getMessage()));
  }

  @ExceptionHandler(RuntimeException.class)
  public ResponseEntity<?> runtimeExceptionHandler(RuntimeException e) {
    log.error("Error occurs in PromotionServer : {}", e.getMessage());
    return ResponseEntity.status(PromotionErrorCode.INTERNAL_SERVER_ERROR.getStatus())
        .body(
            ApiResponse.error(
                PromotionErrorCode.INTERNAL_SERVER_ERROR.getStatus().name(), e.getMessage()));
  }

}
