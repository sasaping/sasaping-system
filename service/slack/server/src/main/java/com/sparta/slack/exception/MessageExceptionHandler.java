package com.sparta.slack.exception;


import com.sparta.common.domain.response.ApiResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
@Slf4j
public class MessageExceptionHandler {

  @ExceptionHandler(MessageException.class)
  public ApiResponse<?> handleException(MessageException e) {
    log.error(e.getMessage());
    return ApiResponse.error(e.getStatusName(), e.getMessage());

  }


}
