package com.sparta.order.server.infrastructure.event;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class PaymentCompletedEvent {

  private Long orderId;
  private Long paymentId;
  private Long userId;
  private Long amount;
  private Boolean success;

}
