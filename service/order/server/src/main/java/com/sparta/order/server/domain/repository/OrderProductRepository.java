package com.sparta.order.server.domain.repository;

import com.sparta.order.server.domain.model.Order;
import com.sparta.order.server.domain.model.OrderProduct;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface OrderProductRepository extends JpaRepository<OrderProduct, Long> {

  List<OrderProduct> findByOrder(Order order);

}
