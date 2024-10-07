package com.sparta.payment.application.dto;


import com.sparta.payment.domain.entity.PaymentState;
import java.time.LocalDateTime;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

public class PaymentHistoryResponse {

  @Getter
  @Setter
  @Builder
  public static class Get {

    private PaymentState type;
    private String cancelReason;
    private Long amount;
    private LocalDateTime createdAt;


  }

}
