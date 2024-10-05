package com.sparta.product.presentation.response;

import com.sparta.product.domain.model.Category;
import java.util.List;
import java.util.stream.Collectors;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class CategoryResponse {
  private Long categoryId;
  private String name;
  private List<CategoryResponse> subCategories;

  public CategoryResponse(Long categoryId, String name, List<CategoryResponse> subCategories) {
    this.categoryId = categoryId;
    this.name = name;
    this.subCategories = subCategories;
  }

  public static CategoryResponse fromEntity(Category category) {
    return new CategoryResponse(
        category.getCategoryId(),
        category.getName(),
        category.getSubCategories().stream()
            .map(CategoryResponse::fromEntity)
            .collect(Collectors.toList()));
  }
}
