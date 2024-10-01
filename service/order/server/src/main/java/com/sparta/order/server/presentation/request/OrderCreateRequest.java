package com.sparta.order.server.presentation.request;

import java.util.ArrayList;
import java.util.List;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@AllArgsConstructor
@NoArgsConstructor
public class OrderCreateRequest {

  private String orderType;
  private List<OrderProductInfo> orderProductInfos = new ArrayList<>();
  private int pointPrice;
  private Long addressId;

}
