package com.sparta.auth.server.exception;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;

@Getter
@RequiredArgsConstructor
public enum AuthErrorCode {
  SIGN_IN_FAIL(HttpStatus.UNAUTHORIZED, "로그인 실패"),
  INTERNAL_SERVER_ERROR(HttpStatus.INTERNAL_SERVER_ERROR, "내부 서버 오류");

  private final HttpStatus status;
  private final String message;
}
