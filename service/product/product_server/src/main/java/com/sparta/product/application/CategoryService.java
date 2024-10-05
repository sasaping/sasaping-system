package com.sparta.product.application;

import com.sparta.product.domain.model.Category;
import com.sparta.product.domain.repository.jpa.CategoryRepository;
import com.sparta.product.presentation.exception.ProductErrorCode;
import com.sparta.product.presentation.exception.ProductServerException;
import com.sparta.product.presentation.response.CategoryResponse;
import java.util.Optional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
@Slf4j(topic = "CategoryService")
public class CategoryService {
  private final CategoryRepository categoryRepository;
  private final CategoryCacheService categoryCacheService;

  @Transactional
  public Long createCategory(String name, Long parentCategoryId) {
    Category parent =
        Optional.ofNullable(parentCategoryId).map(this::findByCategoryId).orElse(null);
    Category category = new Category(name, parent);
    Optional.ofNullable(parent).ifPresent(p -> p.addSubCategory(category));

    var saved = categoryRepository.save(category);
    refreshCategoryCache();
    return saved.getCategoryId();
  }

  @Transactional
  public CategoryResponse updateCategory(
      Long targetCategoryId, String name, Long parentCategoryId) {
    Category target = findByCategoryId(targetCategoryId);
    Category parent =
        Optional.ofNullable(parentCategoryId).map(this::findByCategoryId).orElse(null);

    target.update(name, parent);
    syncParentCategory(target, parent); // 변경된 부모의 기존 연결 끊기
    refreshCategoryCache();

    return CategoryResponse.fromEntity(target);
  }

  @Transactional
  public void deleteCategory(Long categoryId) {
    Category category = findByCategoryId(categoryId);
    categoryRepository.delete(category);
    refreshCategoryCache();
  }

  private void syncParentCategory(Category target, Category newParent) {
    Category oldParent = target.getParent();
    if (oldParent != null) {
      oldParent.removeSubCategory(target);
    }
    if (newParent != null) {
      newParent.addSubCategory(target);
    }
  }

  private void refreshCategoryCache() {
    categoryCacheService.clearCache();
    categoryCacheService.fetchAndCacheCategories();
  }

  private Category findByCategoryId(Long categoryId) {
    return categoryRepository
        .findByCategoryId(categoryId)
        .orElseThrow(() -> new ProductServerException(ProductErrorCode.NOT_FOUND_CATEGORY));
  }
}
