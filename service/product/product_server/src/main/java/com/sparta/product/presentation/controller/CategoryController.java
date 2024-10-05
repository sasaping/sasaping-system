package com.sparta.product.presentation.controller;

import com.sparta.common.domain.response.ApiResponse;
import com.sparta.product.application.CategoryService;
import com.sparta.product.presentation.request.CategoryCreateRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/categories")
@RequiredArgsConstructor
@Validated
public class CategoryController {
  private final CategoryService categoryService;

  @PostMapping
  public ApiResponse<Long> createCategory(@RequestBody CategoryCreateRequest request) {
    return ApiResponse.created(
        categoryService.createCategory(request.name(), request.parentCategoryId()));
  }
}
