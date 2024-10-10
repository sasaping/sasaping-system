package com.sparta.product.application;

import com.sparta.product.presentation.response.CategoryResponse;
import java.util.List;

public interface CategoryCache {
  List<CategoryResponse> fetchAndCacheCategories();

  void clearCache();
}
