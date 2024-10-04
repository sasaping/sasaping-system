package com.sparta.order.server.Cart.exception;

import com.sparta.common.domain.exception.BusinessException;
import com.sparta.common.domain.response.ApiResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@Slf4j
@RestControllerAdvice
public class CartExceptionHandler {

  @ExceptionHandler(BusinessException.class)
  public ApiResponse<Void> handleBusinessException(BusinessException ex) {
    return ApiResponse.error(ex.getStatusName(), ex.getMessage());
  }

  @ExceptionHandler(Exception.class)
  public ApiResponse<Void> handleAllExceptions(Exception ex) {
    return ApiResponse.error(HttpStatus.INTERNAL_SERVER_ERROR.name(), ex.getMessage());
  }

}
