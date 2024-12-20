package com.sparta.notification.server.exception;

import lombok.Getter;
import org.springframework.http.HttpStatus;

@Getter
public enum NotificationErrorCode {
  USER_NOT_FOUND(HttpStatus.NOT_FOUND, "존재하지 않는 사용자입니다. : [%s]"),
  ORDER_NOT_FOUND(HttpStatus.NOT_FOUND, "존재하지 않는 주문입니다. : [%s]"),
  INTERNAL_SERVER_ERROR(HttpStatus.INTERNAL_SERVER_ERROR, "Notification 서비스에서 오류가 발생했습니다."),
  ORDER_SERVICE_UNAVAILABLE(HttpStatus.SERVICE_UNAVAILABLE, "Order 서비스와 통신 중 오류가 발생했습니다."),
  USER_SERVICE_UNAVAILABLE(HttpStatus.SERVICE_UNAVAILABLE, "User 서비스와 통신 중 오류가 발생했습니다.");

  private final HttpStatus status;
  private final String message;

  NotificationErrorCode(HttpStatus status, String message) {
    this.status = status;
    this.message = message;
  }
}
