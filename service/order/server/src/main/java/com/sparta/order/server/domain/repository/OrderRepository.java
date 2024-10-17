package com.sparta.order.server.domain.repository;

import com.sparta.order.server.domain.model.Order;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface OrderRepository extends JpaRepository<Order, Long> {

  boolean existsByOrderNo(String orderNo);

}
