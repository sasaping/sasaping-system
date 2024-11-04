package com.sparta.notification.server.domain.model.vo;

import lombok.Getter;

@Getter
public enum NotificationType {
  ORDER_UPDATE("주문 갱신 알림"),
  RESTOCK("재입고 알림"),
  EVENT("이벤트 알림");

  private String description;

  private NotificationType(String description) {
    this.description = description;
  }
}
