package com.sparta.order.server.presentation.dto;

import java.util.ArrayList;
import java.util.List;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

public class OrderDto {

  @Getter
  @AllArgsConstructor
  @NoArgsConstructor
  public static class OrderCreateRequest {

    private String orderType;
    private List<OrderProductInfo> orderProductInfos = new ArrayList<>();
    private int pointPrice;
    private Long addressId;

  }

  @Getter
  @AllArgsConstructor
  @NoArgsConstructor
  public static class OrderProductInfo {

    private String productId;
    private int quantity;
    private Long userCouponId;

  }

}
