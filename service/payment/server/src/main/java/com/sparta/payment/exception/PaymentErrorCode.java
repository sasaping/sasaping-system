package com.sparta.payment.exception;

import lombok.Getter;
import org.springframework.http.HttpStatus;

@Getter
public enum PaymentErrorCode {
  INVALID_PARAMETER(HttpStatus.INTERNAL_SERVER_ERROR, "결제중 오류가 발생했습니다."),
  PAYMENT_NOT_FOUND(HttpStatus.NOT_FOUND, "결제 정보를 찾을 수 없습니다."),
  ;

  private final HttpStatus status;
  private final String message;

  PaymentErrorCode(HttpStatus status, String message) {
    this.status = status;
    this.message = message;
  }

}
