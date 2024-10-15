package com.sparta.order.server.exception;

import lombok.Getter;
import org.springframework.http.HttpStatus;

@Getter
public enum CartErrorCode {

  PRODUCT_NOT_IN_CART(HttpStatus.BAD_REQUEST, "장바구니에 해당 상품이 존재하지 않습니다."),
  CART_NOT_FOUND(HttpStatus.BAD_REQUEST, "존재하지 않는 장바구니입니다."),
  USER_NOT_FOUND(HttpStatus.BAD_REQUEST, "존재하지 않는 사용자입니다. : [%s]");

  private final HttpStatus status;
  private final String message;

  CartErrorCode(HttpStatus status, String message) {
    this.status = status;
    this.message = message;
  }
}
