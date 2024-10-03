package com.sparta.product.domain.repository.cassandra;

import com.sparta.product.domain.model.Product;
import java.util.Optional;
import java.util.UUID;
import org.springframework.data.cassandra.repository.CassandraRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ProductRepository extends CassandraRepository<Product, UUID> {
  Optional<Product> findByProductId(UUID productId);
}
