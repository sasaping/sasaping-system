package com.sparta.product.presentation.controller;

import com.sparta.common.domain.response.ApiResponse;
import com.sparta.product.application.CategoryService;
import com.sparta.product.presentation.request.CategoryCreateRequest;
import com.sparta.product.presentation.request.CategoryUpdateRequest;
import com.sparta.product.presentation.response.CategoryResponse;
import jakarta.validation.constraints.NotNull;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
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
  public ApiResponse<Long> createCategory(@RequestBody @Validated CategoryCreateRequest request) {
    return ApiResponse.created(
        categoryService.createCategory(request.name(), request.parentCategoryId()));
  }

  @PatchMapping("/{categoryId}")
  public ApiResponse<CategoryResponse> updateCategory(
      @PathVariable("categoryId") @NotNull Long categoryId,
      @RequestBody @Validated CategoryUpdateRequest request) {
    return ApiResponse.ok(
        categoryService.updateCategory(categoryId, request.name(), request.parentCategoryId()));
  }

  @DeleteMapping("/{categoryId}")
  public ApiResponse<Void> deleteCategory(@PathVariable("categoryId") @NotNull Long categoryId) {
    categoryService.deleteCategory(categoryId);
    return ApiResponse.ok();
  }

  @GetMapping
  public ApiResponse<List<CategoryResponse>> getCategories() {
    return ApiResponse.ok(categoryService.fetchAndCacheCategories());
  }
}
