package com.sparta.order.server.domain.model;

public enum OrderState {

  COMPLETED("주문 완료"),
  READY_FOR_SHIPMENT("배송 준비중"),
  SHIPPING("배송중"),
  DELIVERED("배송 완료"),
  PURCHASE_CONFIRMED("구매 확정");
  private final String description;

  private OrderState(String description) {
    this.description = description;
  }

  public String getDescription() {
    return description;
  }
}
