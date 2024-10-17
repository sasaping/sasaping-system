package com.sparta.server.exception;

import com.sparta.common.domain.exception.BusinessException;

public class SearchException extends BusinessException {

  public SearchException(SearchErrorCode errorCode) {
    super(errorCode.getStatus().name(), errorCode.getMessage());
  }

}
