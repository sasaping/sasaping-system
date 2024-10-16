package com.sparta.order.server.presentation.dto;


import com.sparta.product_dto.ProductDto;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import java.math.BigDecimal;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

public class CartDto {

  @Getter
  @NoArgsConstructor
  @AllArgsConstructor
  public static class CartProductRequest {

    @NotNull(message = "상품 ID는 필수 값입니다.")
    private String productId;

    @NotNull(message = "상품 수량은 필수 값입니다.")
    @Min(value = 1, message = "상품 수량 최소 값은 1 입니다.")
    private Integer quantity;

  }

  @Getter
  @NoArgsConstructor
  @AllArgsConstructor
  public static class CartProductResponse {

    private String productId;
    private Integer quantity;

    private String name;
    private BigDecimal originalPrice;
    private BigDecimal discountedPrice;
    private Double discountPercent;

    public static CartProductResponse from(ProductDto dto, Integer quantity) {
      return new CartProductResponse(
          dto.getProductId().toString(),
          quantity,
          dto.getProductName(),
          dto.getOriginalPrice(),
          dto.getDiscountedPrice(),
          dto.getDiscountPercent());
    }

  }

}
