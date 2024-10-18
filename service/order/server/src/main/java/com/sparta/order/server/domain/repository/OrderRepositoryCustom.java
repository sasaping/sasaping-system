package com.sparta.order.server.domain.repository;

import com.sparta.order.server.domain.model.Order;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface OrderRepositoryCustom {

  Page<Order> myOrderFind(Pageable pageable, Long userId, String keyword);

}
