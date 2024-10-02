package com.sparta.order.server.Cart.presentation.request;


import com.sparta.order.server.Cart.domain.model.ProductInfo;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

public class CartRequest {

  @Getter
  @NoArgsConstructor
  @AllArgsConstructor
  public static class Add {

    // TODO 인증 인가 구현되면 제외 시키기
    private Long userId;
    private String productId;
    private ProductInfoRequest productInfoRequest;

  }

  @Getter
  @NoArgsConstructor
  @AllArgsConstructor
  public static class ProductInfoRequest {

    private String productName;
    private int quantity;
    private int price;

    public ProductInfo toEntity() {
      return new ProductInfo(productName, quantity, price);
    }

  }

}
