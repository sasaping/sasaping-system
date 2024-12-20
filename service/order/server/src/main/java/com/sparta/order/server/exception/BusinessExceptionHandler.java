package com.sparta.order.server.exception;

import com.sparta.common.domain.exception.BusinessException;
import com.sparta.common.domain.response.ApiResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@Slf4j
@RestControllerAdvice
public class BusinessExceptionHandler {

  @ExceptionHandler(BusinessException.class)
  public ApiResponse<Void> handleBusinessException(BusinessException ex) {
    log.error(ex.getStatusName(), ex.getMessage());
    return ApiResponse.error(ex.getStatusName(), ex.getMessage());
  }

  @ExceptionHandler(MethodArgumentNotValidException.class)
  public ApiResponse<Void> handleValidationExceptions(MethodArgumentNotValidException ex) {
    ex.printStackTrace();
    String message = ex.getBindingResult().getAllErrors().get(0).getDefaultMessage();
    return ApiResponse.error(HttpStatus.BAD_REQUEST.name(), message);
  }

  @ExceptionHandler(Exception.class)
  public ApiResponse<Void> handleAllExceptions(Exception ex) {
    ex.printStackTrace();
    return ApiResponse.error(HttpStatus.INTERNAL_SERVER_ERROR.name(), ex.getMessage());
  }
}
