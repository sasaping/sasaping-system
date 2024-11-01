package com.sparta.product.presentation.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import java.math.BigDecimal;
import java.util.List;
import java.util.UUID;

public record ProductUpdateRequest(
    @NotNull(message = "상품아이디는 필수입니다") UUID productId,
    @NotNull(message = "카테고리아이디는 필수입니다") Long categoryId,
    @NotBlank(message = "상품이름은 필수입니다") String productName,
    @NotBlank(message = "브랜드이름은 필수입니다") String brandName,
    @NotBlank(message = "메인컬러는 필수입니다") String mainColor,
    @NotBlank(message = "상품사이즈는 필수입니다") String size,
    @NotNull(message = "상품가격은 필수입니다") BigDecimal originalPrice,
    Double discountPercent,
    @NotNull(message = "상품재고수량은 필수입니다") Integer stock,
    @NotBlank(message = "상품설명은 필수입니다") String description,
    @NotNull(message = "상품이름은 필수입니다") Integer limitCountPerUser,
    @NotNull(message = "공개여부는 필수입니다") Boolean isPublic,
    List<String> tags) {}
