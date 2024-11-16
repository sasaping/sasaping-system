package com.sparta.notification.server.exception;

import com.sparta.common.domain.exception.BusinessException;

public class NotificationException extends BusinessException {

  private final NotificationErrorCode errorCode;

  public NotificationException(NotificationErrorCode errorCode, Object... args) {
    super(errorCode.getStatus().name(), errorCode.getMessage(), args);
    this.errorCode = errorCode;
  }

}
