package com.sparta.payment_dto.infrastructure;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

public class PaymentInternalDto {

  @Getter
  @Setter
  @AllArgsConstructor
  public static class Create {

    private Long userId;
    private Long orderId;
    private String orderName;
    private String email;
    private Long amount;

  }

  @Getter
  @Setter
  @AllArgsConstructor
  public static class Cancel {

    private Long orderId;
    private String cancelReason;

  }

}
