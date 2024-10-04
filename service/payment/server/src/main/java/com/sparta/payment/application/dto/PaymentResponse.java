package com.sparta.payment.application.dto;

import lombok.Getter;
import lombok.Setter;

public class PaymentResponse {

  @Getter
  @Setter
  public static class Create {

    private String paymentKey;
    private Long orderId;
    private String orderName;
    private String status;
    private String requestedAt;
    private Long totalAmount;
    private Object checkout;

  }

}
