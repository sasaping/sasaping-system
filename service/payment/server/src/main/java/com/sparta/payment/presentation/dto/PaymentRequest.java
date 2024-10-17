package com.sparta.payment.presentation.dto;

import lombok.Getter;
import lombok.Setter;

public class PaymentRequest {

  @Getter
  @Setter
  public static class Create {

    private Long userId;
    private Long orderId;
    private String orderName;
    private String email;
    private Long amount;

  }

  @Getter
  @Setter
  public static class CreateExt extends Create {

    private String flowMode = "DIRECT";
    private String easyPay = "토스페이";
    private String method = "";
    private String successUrl;
    private String failUrl;

  }

  @Getter
  @Setter
  public static class Confirm {

    private String paymentKey;
    private Long orderId;
    private Long amount;

  }

  @Getter
  @Setter
  public static class Cancel {

    private Long orderId;
    private String cancelReason;

  }

  @Getter
  @Setter
  public static class Search {

    private String keyword;
    private Long userId;
    private String startDate;
    private String endDate;


  }

}
