package com.sparta.order.server.domain.repository;

import static com.sparta.order.server.domain.model.QOrder.order;
import static com.sparta.order.server.domain.model.QOrderProduct.orderProduct;

import com.querydsl.jpa.impl.JPAQuery;
import com.querydsl.jpa.impl.JPAQueryFactory;
import com.sparta.order.server.domain.model.Order;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;

@RequiredArgsConstructor
public class OrderRepositoryImpl implements OrderRepositoryCustom {

  private final JPAQueryFactory queryFactory;

  @Override
  public Page<Order> getMyOrder(Pageable pageable, Long userId, String keyword) {

    JPAQuery<Order> query = queryFactory.selectFrom(order)
        .join(order.orderProducts, orderProduct).fetchJoin()
        .where(order.userId.eq(userId));

    if (keyword != null && !keyword.trim().isEmpty()) {
      query.where(orderProduct.productName.containsIgnoreCase(keyword));
    }

    List<Order> orders = query
        .offset(pageable.getOffset())
        .limit(pageable.getPageSize())
        .fetch();

    Long totalSize = queryFactory.select(order.count())
        .from(order)
        .join(order.orderProducts, orderProduct)
        .where(order.userId.eq(userId).and(keyword != null && !keyword.trim().isEmpty()
            ? orderProduct.productName.containsIgnoreCase(keyword)
            : null))
        .fetchOne();

    long totalElements = (totalSize != null) ? totalSize : 0;

    return new PageImpl<>(orders, pageable, totalElements);
  }

  @Override
  public Page<Order> getAllOrder(Pageable pageable, Long orderUserId, String productId) {
    JPAQuery<Order> query = queryFactory.selectFrom(order)
        .join(order.orderProducts, orderProduct).fetchJoin();

    if (orderUserId != null) {
      query.where(order.userId.eq(orderUserId));
    }

    if (productId != null && !productId.trim().isEmpty()) {
      query.where(orderProduct.productId.eq(productId));
    }

    List<Order> orders = query
        .offset(pageable.getOffset())
        .limit(pageable.getPageSize())
        .fetch();

    Long totalSize = queryFactory.select(order.count())
        .from(order)
        .join(order.orderProducts, orderProduct)
        .where(orderUserId != null ?
                order.userId.eq(orderUserId) : null,
            productId != null && !productId.trim().isEmpty() ?
                orderProduct.productId.eq(productId) : null)
        .fetchOne();

    long totalElements = (totalSize != null) ? totalSize : 0;

    return new PageImpl<>(orders, pageable, totalElements);
  }

}
