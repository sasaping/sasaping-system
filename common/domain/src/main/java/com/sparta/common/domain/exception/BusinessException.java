package com.sparta.common.domain.exception;

import lombok.Getter;

@Getter
public abstract class BusinessException extends RuntimeException {

  protected String statusName;
  private final String message;

  public BusinessException(String statusName, String message) {
    super(message);
    this.statusName = statusName;
    this.message = message;
  }

  public BusinessException(String statusName, String message, Object... args) {
    super(formattingErrorMessage(message, args));
    this.statusName = statusName;
    this.message = formattingErrorMessage(message, args);
  }

  private static String formattingErrorMessage(String message, Object... objects) {
    return message.formatted(objects);
  }

}
