package com.sparta.product.presentation.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import java.math.BigDecimal;
import java.util.UUID;

public record ProductUpdateRequest(
    @NotNull(message = "상품아이디는 필수입니다") UUID productId,
    @NotNull(message = "카테고리아이디는 필수입니다") Long categoryId,
    @NotBlank(message = "상품이름은 필수입니다") String productName,
    @NotNull(message = "상품가격은 필수입니다") BigDecimal originalPrice,
    Double discountPercent,
    @NotNull(message = "상품재고수량은 필수입니다") Integer stock,
    @NotBlank(message = "상품설명은 필수입니다") String description,
    @NotBlank(message = "썸네일이미지주소는 필수입니다") String thumbnailImgUrl,
    @NotBlank(message = "상세이미지주소는 필수입니다") String detailImgUrl,
    @NotNull(message = "상품이름은 필수입니다") Integer limitCountPerUser,
    @NotNull(message = "공개여부는 필수입니다") Boolean isPublic,
    @NotNull(message = "쿠폰적용여부는 필수입니다") Boolean isCoupon) {

}
