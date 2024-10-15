package com.sparta.order.server.exception;

import lombok.Getter;
import org.springframework.http.HttpStatus;

@Getter
public enum OrderErrorCode {
  ORDER_NOT_FOUND(HttpStatus.NOT_FOUND, "존재하지 않는 주문입니다."),
  CANNOT_CANCEL_WHILE_SHIPPING(HttpStatus.FORBIDDEN, "배송이 시작된 주문은 취소할 수 없습니다. : [%s]"),
  INSUFFICIENT_POINT(HttpStatus.BAD_REQUEST, "포인트가 부족합니다."),
  ADDRESS_MISMATCH(HttpStatus.BAD_REQUEST, "배송지 정보가 사용자가 등록한 배송지와 일치하지 않습니다. : [%s]"),
  ORDER_NO_GENERATION_FAILED(
      HttpStatus.INTERNAL_SERVER_ERROR,
      "주문 번호 생성에 실패했습니다. 잠시 후 다시 시도해주세요."),
  INSUFFICIENT_STOCK(HttpStatus.BAD_REQUEST, "상품의 재고가 부족합니다. : [%s]"),
  COUPON_NOT_APPLICABLE(HttpStatus.BAD_REQUEST, "쿠폰 적용이 불가한 상품입니다. : [%s]"),
  CART_ITEM_ONLY_ORDERABLE(HttpStatus.BAD_REQUEST, "장바구니에 있는 상품만 주문이 가능합니다."),
  CART_ITEM_QUANTITY_MISMATCH(HttpStatus.BAD_REQUEST, "장바구니에 있는 상품과 주문 상품의 수량이 다릅니다. : [%s]");


  private final HttpStatus status;
  private final String message;

  OrderErrorCode(HttpStatus status, String message) {
    this.status = status;
    this.message = message;
  }
}
