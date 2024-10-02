package com.sparta.order.server.presentation.request;

import java.util.UUID;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@AllArgsConstructor
@NoArgsConstructor
public class OrderProductInfo {

  private UUID productId;
  private int quantity;
  private int price;
  private Long couponId;

}
