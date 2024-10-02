package com.sparta.order.server.domain.model;

import com.sparta.common.domain.entity.BaseEntity;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import jakarta.persistence.UniqueConstraint;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "p_order",
    uniqueConstraints = @UniqueConstraint(columnNames = "order_no", name = "UK_ORDER_NO"))
public class Order extends BaseEntity {

  @Id
  @GeneratedValue(strategy = GenerationType.AUTO)
  private Long orderId;

  @Column(name = "user_id", nullable = false)
  private Long userId;

  @Column(name = "payment_id", nullable = false)
  private Long paymentId;

  @Column(name = "order_no", nullable = false, unique = true)
  private String orderNo;

  @Column(nullable = false)
  @Enumerated(value = EnumType.STRING)
  private OrderType type;

  @Column(nullable = false)
  @Enumerated(value = EnumType.STRING)
  private OrderState state;

  @Column(name = "total_quantity", nullable = false)
  private int totalQuantity;

  @Column(name = "total_amount", nullable = false)
  private float totalAmount;

  @Column(name = "shipping_amount", nullable = false)
  private float shippingAmount;

  @Column(name = "total_real_amount", nullable = false)
  private float totalRealAmount;

  @Column(name = "point_price", nullable = false)
  private int pointPrice;

  @Column(name = "coupon_price", nullable = false)
  private int couponPrice;

  @Column(name = "invoice_number", nullable = true)
  private String invoiceNumber;

  @Column(nullable = false)
  private String recipient;

  @Column(name = "phone_number", nullable = false)
  private String phoneNumber;

  @Column(nullable = false)
  private String zipcode;

  @Column(name = "shipping_address", nullable = false)
  private String shippingAddress;


}
