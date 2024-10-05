package com.sparta.product.application;

import com.sparta.product.domain.repository.jpa.CategoryRepository;
import com.sparta.product.presentation.response.CategoryResponse;
import java.util.List;
import java.util.stream.Collectors;
import lombok.RequiredArgsConstructor;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class CategoryCacheService implements CategoryCache {
  private final CategoryRepository categoryRepository;

  @Cacheable(cacheNames = "categories-cache", key = "'categories'")
  @Override
  public List<CategoryResponse> fetchAndCacheCategories() {
    return categoryRepository.findAllWithSubCategories().stream()
        .filter(category -> category.getParent() == null)
        .map(CategoryResponse::fromEntity)
        .collect(Collectors.toList());
  }

  @CacheEvict(cacheNames = "categories-cache", key = "'categories'")
  @Override
  public void clearCache() {}
}
