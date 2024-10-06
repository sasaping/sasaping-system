package com.sparta.order.server.Cart.presentation.dto;


import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

public class CartDto {

  @Getter
  @NoArgsConstructor
  @AllArgsConstructor
  public static class Request {

    // TODO 인증 인가 구현되면 제외 시키기
    private Long userId;

    @NotNull(message = "상품 ID는 필수 값입니다.")
    private String productId;

    @NotNull(message = "상품 수량은 필수 값입니다.")
    @Min(value = 1, message = "상품 수량 최소 값은 1 입니다.")
    private Integer quantity;

  }

}
