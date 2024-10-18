package com.sparta.order.server.domain.repository;

import com.sparta.order.server.domain.model.Order;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

@Repository
public interface OrderRepository extends JpaRepository<Order, Long>, OrderRepositoryCustom {

  boolean existsByOrderNo(String orderNo);

  @Query("SELECT o FROM Order o LEFT JOIN FETCH o.orderProducts WHERE o.orderId = :orderId")
  Optional<Order> findById(Long orderId);

}
