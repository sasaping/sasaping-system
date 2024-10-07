package com.sparta.payment.exception;

import com.sparta.common.domain.response.ApiResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
@Slf4j
public class PaymentExceptionHandler {

  @ExceptionHandler(PaymentException.class)
  public ApiResponse<?> handleException(PaymentException e) {
    log.error(e.getMessage());
    return ApiResponse.error(e.getStatusName(), e.getMessage());

  }

}
