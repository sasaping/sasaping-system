package com.sparta.payment.domain.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import java.time.LocalDateTime;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Getter
@Setter
@NoArgsConstructor
@Table(name = "p_payment_history")
public class PaymentHistory {

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  @Column(name = "payment_history_id")
  private Long paymentHistoryId;

  @JoinColumn(name = "payment_id")
  @ManyToOne
  private Payment payment;

  private Long amount;

  @Enumerated(EnumType.STRING)
  private PaymentState type;

  private String cancelReason;

  private LocalDateTime createdAt;

  public static PaymentHistory create(Payment payment) {
    PaymentHistory paymentHistory = new PaymentHistory();
    paymentHistory.setPayment(payment);
    paymentHistory.setAmount(payment.getAmount());
    paymentHistory.setType(PaymentState.PAYMENT);
    paymentHistory.setCreatedAt(LocalDateTime.now());
    return paymentHistory;
  }

  public static PaymentHistory cancel(Payment payment) {
    PaymentHistory paymentHistory = new PaymentHistory();
    paymentHistory.setPayment(payment);
    paymentHistory.setAmount(payment.getAmount());
    paymentHistory.setType(PaymentState.CANCEL);
    paymentHistory.setCreatedAt(LocalDateTime.now());
    return paymentHistory;
  }

}
