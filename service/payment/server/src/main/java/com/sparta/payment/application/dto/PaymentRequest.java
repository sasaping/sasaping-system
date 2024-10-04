package com.sparta.payment.application.dto;

import lombok.Getter;
import lombok.Setter;

public class PaymentRequest {

  @Getter
  @Setter
  public static class Create {

    private String userId;
    private Long orderId;
    private String orderName;
    private Long amount;

  }

  @Getter
  @Setter
  public static class CreateExt extends Create {

    private String flowMode = "DIRECT";
    private String easyPay = "토스페이";
    private String method = "";
    private String successuUrl = "";
    private String failUrl = "";

  }

}
