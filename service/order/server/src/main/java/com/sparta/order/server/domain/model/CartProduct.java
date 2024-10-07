package com.sparta.order.server.domain.model;

import java.util.UUID;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
@AllArgsConstructor
public class CartProduct {

  private UUID productId;
  private ProductInfo productInfo;

}
