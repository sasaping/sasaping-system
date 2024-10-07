package com.sparta.payment.exception;

import com.sparta.common.domain.exception.BusinessException;

public class PaymentException extends BusinessException {

  public PaymentException(PaymentErrorCode errorCode) {
    super(errorCode.getStatus().name(), errorCode.getMessage());
  }

}
