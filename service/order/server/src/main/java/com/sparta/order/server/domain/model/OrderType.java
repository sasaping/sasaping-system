package com.sparta.order.server.domain.model;

import lombok.Getter;

@Getter
public enum OrderType {

  STANDARD("기본"),
  PREORDER("사전예약"),
  RAFFLE("래플");

  private final String description;

  private OrderType(String description) {
    this.description = description;
  }

  public String getDescription() {
    return description;
  }

}
