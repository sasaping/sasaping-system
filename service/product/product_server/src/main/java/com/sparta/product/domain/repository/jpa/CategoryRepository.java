package com.sparta.product.domain.repository.jpa;

import com.sparta.product.domain.model.Category;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CategoryRepository extends JpaRepository<Category, Long> {
  Optional<Category> findByCategoryId(Long categoryId);
}
