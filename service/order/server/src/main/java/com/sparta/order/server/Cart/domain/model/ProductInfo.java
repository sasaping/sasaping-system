package com.sparta.order.server.Cart.domain.model;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.ToString;

@Getter
@ToString
@NoArgsConstructor
@AllArgsConstructor
public class ProductInfo {

  private String productName;
  private int quantity;
  private int price;

  public void addQuantity(int quantity) {
    this.quantity += quantity;
  }

}
