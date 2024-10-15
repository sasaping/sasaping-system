package com.sparta.order.server.domain.model.vo;

import lombok.Getter;

@Getter
public enum OrderState {

  PENDING_PAYMENT("결제 대기중"),
  COMPLETED("주문 완료"),
  READY_FOR_SHIPMENT("배송 준비중"),
  SHIPPING("배송중"),
  DELIVERED("배송 완료"),
  PURCHASE_CONFIRMED("구매 확정"),
  CANCELED("주문 취소");

  private final String description;

  private OrderState(String description) {
    this.description = description;
  }

  public String getDescription() {
    return description;
  }
}
