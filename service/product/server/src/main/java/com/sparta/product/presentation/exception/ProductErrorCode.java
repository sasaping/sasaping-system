package com.sparta.product.presentation.exception;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;

@Getter
@RequiredArgsConstructor
public enum ProductErrorCode {
  NOT_FOUND_PRODUCT(HttpStatus.NOT_FOUND, "상품이 존재하지 않습니다"),
  NOT_FOUND_CATEGORY(HttpStatus.NOT_FOUND, "카테고리가 존재하지 않습니다"),
  NOT_FOUND_PREORDER(HttpStatus.NOT_FOUND, "사전예약정보가 존재하지 않습니다"),
  PREORDER_QUANTITY_CONFLICT(HttpStatus.CONFLICT, "예약가능수량이 재고량보다 많습니다"),

  INTERNAL_SERVER_ERROR(HttpStatus.INTERNAL_SERVER_ERROR, "내부 서버 오류입니다");

  private final HttpStatus status;
  private final String message;
}
