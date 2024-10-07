package com.sparta.order.server.presentation.dto;


import com.fasterxml.jackson.annotation.JsonProperty;
import com.sparta.order.server.domain.model.ProductInfo;
import jakarta.validation.Valid;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
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

    @NotNull(message = "상품 ID는 필수 값입니다.")
    private String productId;

    @Valid
    private ProductInfoDto productInfoDto;

  }

  @Getter
  @NoArgsConstructor
  @AllArgsConstructor
  public static class UpdateRequest {

    // TODO 인증 인가 구현되면 제외 시키기
    private Long userId;

    @NotNull(message = "상품 ID는 필수 값입니다.")
    private String productId;

    @NotNull(message = "상품 수량은 필수 값입니다.")
    @Min(value = 1, message = "상품 수량 최소 값은 1 입니다.")
    private Integer quantity;

  }

  @Getter
  @NoArgsConstructor
  @AllArgsConstructor
  public static class ProductInfoDto {

    @JsonProperty("productName")
    @NotNull(message = "상품 ID는 필수 값입니다.")
    private String productName;

    @JsonProperty("quantity")
    @NotNull(message = "상품 수량은 필수 값입니다.")
    @Min(value = 1, message = "상품 수량 최소 값은 1 입니다.")
    private Integer quantity;

    @JsonProperty("price")
    @NotNull(message = "상품 가격은 필수 값입니다.")
    @Min(value = 1, message = "상품 가격 최소 값은 1 입니다.")
    private Integer price;

    public ProductInfo toEntity() {
      return new ProductInfo(productName, quantity, price);
    }

    public static ProductInfoDto fromModel(ProductInfo model) {
      return new ProductInfoDto(model.getProductName(), model.getQuantity(), model.getPrice());
    }

  }

}
