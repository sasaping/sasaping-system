package com.sparta.product.domain.repository.jpa;

import com.sparta.product.domain.model.PreOrder;
import java.util.Optional;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface PreOrderRepository extends JpaRepository<PreOrder, Long> {
  Optional<PreOrder> findByPreOrderIdAndIsPublicTrue(Long preOrderId);

  Page<PreOrder> findAllByIsPublicTrue(Pageable pageable);
}
