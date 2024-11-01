package com.sparta.payment.domain.repository;

import com.sparta.payment.domain.entity.Payment;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface PaymentRepository extends JpaRepository<Payment, Long>, PaymentRepositoryCustom {

  Payment findByPaymentKey(String paymentKey);

  Payment findByOrderId(Long orderId);

  Payment findByPaymentId(Long paymentId);

}
