package com.sparta.product.domain.repository.jpa;

import com.sparta.product.domain.model.Category;
import java.util.List;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

public interface CategoryRepository extends JpaRepository<Category, Long> {
  Optional<Category> findByCategoryId(Long categoryId);

  @Query("SELECT c FROM Category c LEFT JOIN FETCH c.subCategories")
  List<Category> findAllWithSubCategories();
}
