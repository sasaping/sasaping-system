package com.sparta.product.domain.repository.cassandra;

import com.sparta.product.domain.model.Product;
import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;
import java.util.UUID;
import org.springframework.data.cassandra.repository.CassandraRepository;
import org.springframework.data.cassandra.repository.Query;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Repository;

@Repository
public interface ProductRepository extends CassandraRepository<Product, UUID> {
  Optional<Product> findByProductIdAndIsDeletedFalse(UUID productId);

  @Query(
      "SELECT * FROM \"P_PRODUCT\" WHERE "
          + "(categoryId = ?0) AND "
          + "(brandName = ?1) AND "
          + "(originalPrice >= ?2) AND "
          + "(originalPrice <= ?3) AND "
          + "(size = ?4) AND "
          + "(mainColor = ?5) ALLOW FILTERING")
  List<Product> findAllByFilters(
      Long categoryId,
      String brandName,
      BigDecimal minPrice,
      BigDecimal maxPrice,
      String productSize,
      String mainColor,
      Pageable pageable);
}
