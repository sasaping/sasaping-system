package com.sparta.payment.domain.entity;

import com.sparta.payment.application.dto.PaymentRequest;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import java.time.LocalDateTime;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Builder(access = AccessLevel.PRIVATE)
@Entity
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Table(name = "p_payment")
public class Payment {

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  @Column(name = "payment_id")
  private Long paymentId;

  @Column(name = "order_id")
  private Long orderId;

  @Column(name = "order_name")
  private String orderName;

  @Column(name = "user_id")
  private Long userId;

  @Column(name = "state")
  @Enumerated(EnumType.STRING)
  private PaymentState state;

  @Column(name = "amount")
  private Long amount;

  @Column(name = "payment_key")
  private String paymentKey;

  @Column(name = "created_at")
  private LocalDateTime createdAt;


  public static Payment create(PaymentRequest.Create request) {
    return Payment.builder()
        .userId(request.getUserId())
        .orderId(request.getOrderId())
        .orderName(request.getOrderName())
        .state(PaymentState.PENDING)
        .amount(request.getAmount())
        .createdAt(LocalDateTime.now())
        .build();
  }

}
