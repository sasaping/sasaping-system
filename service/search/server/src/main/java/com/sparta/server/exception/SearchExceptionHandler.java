package com.sparta.server.exception;

import com.sparta.common.domain.response.ApiResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
@Slf4j
public class SearchExceptionHandler {

  @ExceptionHandler(SearchException.class)
  public ApiResponse<?> handleException(SearchException e) {
    log.error(e.getMessage());
    return ApiResponse.error(e.getStatusName(), e.getMessage());

  }

}
