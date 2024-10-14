package com.sparta.order.server.domain.model;

import com.sparta.common.domain.entity.BaseEntity;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import java.math.BigDecimal;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor(access = AccessLevel.PROTECTED)
@Builder(access = AccessLevel.PROTECTED)
@Table(name = "P_ORDER_PRODUCT")
public class OrderProduct extends BaseEntity {

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long orderProductId;
  @ManyToOne(fetch = FetchType.LAZY)
  @JoinColumn(name = "order_id", nullable = false)
  private Order order;
  @Column(nullable = false)
  private String productId;
  @Column(nullable = false)
  private int quantity;
  @Column(nullable = false)
  private BigDecimal purchasePrice;
  @Column
  private Long userCouponId;
  @Column
  private BigDecimal couponPrice;


  // TODO productId -> productDto 설정
  public static OrderProduct createOrderProduct(String productId, BigDecimal productPrice,
      int quantity, String couponDto, Order order) {

    return OrderProduct.builder()
        .order(order)
        .productId(productId)
        .quantity(quantity)
        .purchasePrice(
            couponDto != null ? productPrice.multiply(BigDecimal.valueOf(quantity))
                .subtract(BigDecimal.valueOf(100))
                : productPrice.multiply(BigDecimal.valueOf(quantity)))
        .userCouponId(couponDto != null ? 1L : null)
        .couponPrice(couponDto != null ? BigDecimal.valueOf(100) : BigDecimal.ZERO)
        .build();
  }


}
