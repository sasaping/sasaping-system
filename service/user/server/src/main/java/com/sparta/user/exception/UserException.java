package com.sparta.user.exception;

import com.sparta.common.domain.exception.BusinessException;

public class UserException extends BusinessException {

  public UserException(UserErrorCode errorCode) {
    super(errorCode.getStatus().name(), errorCode.getMessage());
  }
}
