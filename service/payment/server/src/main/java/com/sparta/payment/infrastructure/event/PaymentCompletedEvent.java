package com.sparta.payment.infrastructure.event;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Builder
@Getter
@Setter
public class PaymentCompletedEvent {

  private Boolean success;
  private Long orderId;
  private Long paymentId;
  private Long userId;
  private Long amount;


}
