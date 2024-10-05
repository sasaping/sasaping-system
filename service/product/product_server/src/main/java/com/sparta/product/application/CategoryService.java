package com.sparta.product.application;

import com.sparta.product.domain.model.Category;
import com.sparta.product.domain.repository.jpa.CategoryRepository;
import com.sparta.product.presentation.exception.ProductErrorCode;
import com.sparta.product.presentation.exception.ProductServerException;
import com.sparta.product.presentation.response.CategoryResponse;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class CategoryService {
  private final CategoryRepository categoryRepository;

  @Transactional
  public Long createCategory(String name, Long parentCategoryId) {
    Category parent =
        Optional.ofNullable(parentCategoryId).map(this::findByCategoryId).orElse(null);
    Category category = new Category(name, parent);
    Optional.ofNullable(parent).ifPresent(p -> p.addSubCategory(category));

    var saved = categoryRepository.save(category);
    return saved.getCategoryId();
  }

  @Transactional
  public CategoryResponse updateCategory(
      Long targetCategoryId, String name, Long parentCategoryId) {
    Category target = findByCategoryId(targetCategoryId);
    Category parent =
        Optional.ofNullable(parentCategoryId).map(this::findByCategoryId).orElse(null);
    target.update(name, parent);
    return CategoryResponse.fromEntity(target);
  }

  @Transactional
  public void deleteCategory(Long categoryId) {
    Category category = findByCategoryId(categoryId);
    categoryRepository.delete(category);
  }

  public List<CategoryResponse> getCategories() {
    return categoryRepository.findAllWithSubCategories().stream()
        .filter(category -> category.getParent() == null)
        .map(CategoryResponse::fromEntity)
        .collect(Collectors.toList());
  }

  private Category findByCategoryId(Long categoryId) {
    return categoryRepository
        .findByCategoryId(categoryId)
        .orElseThrow(() -> new ProductServerException(ProductErrorCode.NOT_FOUND_CATEGORY));
  }
}
