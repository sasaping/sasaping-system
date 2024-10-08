package com.sparta.order.server.Cart.exception;

import com.sparta.common.domain.exception.BusinessException;

public class CartException extends BusinessException {

  private final CartErrorCode errorCode;

  public CartException(CartErrorCode errorCode, Object... args) {
    super(errorCode.getStatus().name(), errorCode.getMessage(), args);
    this.errorCode = errorCode;
  }
}
