package com.sparta.product.presentation.request;

import jakarta.validation.constraints.NotBlank;

public record CategoryUpdateRequest(
    @NotBlank(message = "카테고리명은 필수입니다") String name, Long parentCategoryId) {}
