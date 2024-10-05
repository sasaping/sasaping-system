package com.sparta.product.application;

import com.sparta.product.domain.model.Category;
import com.sparta.product.domain.repository.jpa.CategoryRepository;
import com.sparta.product.presentation.exception.ProductErrorCode;
import com.sparta.product.presentation.exception.ProductServerException;
import java.util.Optional;
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
    var saved = categoryRepository.save(category);
    return saved.getCategoryId();
  }

  private Category findByCategoryId(Long categoryId) {
    return categoryRepository
        .findByCategoryId(categoryId)
        .orElseThrow(() -> new ProductServerException(ProductErrorCode.NOT_FOUND_CATEGORY));
  }
}
