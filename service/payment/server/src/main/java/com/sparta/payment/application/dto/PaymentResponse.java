package com.sparta.payment.application.dto;

import com.sparta.payment.domain.entity.PaymentState;
import java.time.LocalDateTime;
import java.util.List;
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

  @Getter
  @Setter
  public static class Get {

    private Long paymentId;
    private Long orderId;
    private PaymentState state;
    private String orderName;
    private Long amount;
    private LocalDateTime createdAt;
    private List<PaymentHistoryDto> histories;

  }

  @Getter
  @Setter
  public static class PaymentHistoryDto {

    private Long amount;
    private PaymentState type;
    private String cancelReason;
    private LocalDateTime createdAt;

  }

  @Getter
  @Setter
  public static class GetByOrderId {

    private Long paymentId;
    private Long orderId;
    private String orderName;
    private PaymentState state;
    private Long amount;
    private LocalDateTime createdAt;

  }

}
