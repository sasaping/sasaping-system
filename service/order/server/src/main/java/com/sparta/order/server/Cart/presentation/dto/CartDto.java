package com.sparta.order.server.Cart.presentation.dto;


import com.fasterxml.jackson.annotation.JsonProperty;
import com.sparta.order.server.Cart.domain.model.ProductInfo;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

public class CartDto {

  @Getter
  @NoArgsConstructor
  @AllArgsConstructor
  public static class AddRequest {

    // TODO 인증 인가 구현되면 제외 시키기
    private Long userId;
    private String productId;
    private ProductInfoDto productInfoDto;

  }

  @Getter
  @NoArgsConstructor
  @AllArgsConstructor
  public static class ProductInfoDto {

    @JsonProperty("productName")
    private String productName;
    @JsonProperty("quantity")
    private int quantity;
    @JsonProperty("price")
    private int price;

    public ProductInfo toEntity() {
      return new ProductInfo(productName, quantity, price);
    }

    public static ProductInfoDto fromModel(ProductInfo model) {
      return new ProductInfoDto(model.getProductName(), model.getQuantity(), model.getPrice());
    }

  }

}
