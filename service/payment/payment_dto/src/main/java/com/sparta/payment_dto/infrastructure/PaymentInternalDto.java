package com.sparta.payment_dto.infrastructure;

import java.time.LocalDateTime;
import java.util.List;
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

  public enum PaymentState {
    // 결제 완료, 결제 대기, 결제 실패, 결제 취소, 환불 대기, 환불 완료
    PAYMENT, PENDING, FAILED, CANCEL, REFUND_PENDING, REFUND

  }

}
