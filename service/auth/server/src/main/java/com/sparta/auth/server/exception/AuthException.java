package com.sparta.auth.server.exception;

import com.sparta.common.domain.exception.BusinessException;

public class AuthException extends BusinessException {

  public AuthException(AuthErrorCode errorCode) {
    super(errorCode.getStatus().name(), errorCode.getMessage());
  }

}
