package com.sparta.notification.server.domain.model.vo;

import lombok.Getter;

@Getter
public enum NotificationType {
  ORDER("주문 알림"),
  RESTOCK("재입고 알림"),
  EVENT("이벤트 알림");

  private String description;

  private NotificationType(String description) {
    this.description = description;
  }

  public boolean isOrder() {
    return this == ORDER;
  }

  public boolean isRestock() {
    return this == RESTOCK;
  }

  public boolean isEvent() {
    return this == EVENT;
  }
}
