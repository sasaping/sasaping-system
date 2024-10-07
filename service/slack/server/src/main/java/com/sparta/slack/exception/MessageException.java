package com.sparta.slack.exception;

import com.sparta.common.domain.exception.BusinessException;

public class MessageException extends BusinessException {

  public MessageException(MessageErrorCode errorCode) {
    super(errorCode.getStatus().name(), errorCode.getMessage());
  }
}
