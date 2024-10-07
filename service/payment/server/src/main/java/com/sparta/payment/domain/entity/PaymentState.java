package com.sparta.payment.domain.entity;

public enum PaymentState {
  // 결제 완료, 결제 대기, 결제 실패, 결제 취소, 환불 대기, 환불 완료
  PAYMENT, PENDING, FAILED, CANCEL, REFUND_PENDING, REFUND

}
