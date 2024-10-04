package com.sparta.slack.exception;

import lombok.Getter;
import org.springframework.http.HttpStatus;

@Getter
public enum MessageErrorCode {
  USER_NOT_FOUND(HttpStatus.BAD_REQUEST, "사용자를 찾을 수 없습니다."),
  INVALID_PARAMETER(HttpStatus.INTERNAL_SERVER_ERROR, "파라미터가 잘못되었습니다."),
  ;

  private final HttpStatus status;
  private final String message;

  MessageErrorCode(HttpStatus status, String message) {
    this.status = status;
    this.message = message;
  }
}
