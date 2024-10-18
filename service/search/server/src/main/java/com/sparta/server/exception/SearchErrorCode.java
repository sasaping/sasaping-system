package com.sparta.server.exception;

import lombok.Getter;
import org.springframework.http.HttpStatus;

@Getter
public enum SearchErrorCode {
  SEARCH_ERROR(HttpStatus.INTERNAL_SERVER_ERROR, "검색 중 오류가 발생했습니다."),
  KEYWORD_IS_EMPTY(HttpStatus.BAD_REQUEST, "검색어가 비어있습니다."),
  ;

  private final HttpStatus status;
  private final String message;

  SearchErrorCode(HttpStatus status, String message) {
    this.status = status;
    this.message = message;
  }

}
