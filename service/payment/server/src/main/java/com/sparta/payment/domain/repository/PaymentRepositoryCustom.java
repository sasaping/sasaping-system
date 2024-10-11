package com.sparta.payment.domain.repository;

import com.sparta.payment.domain.entity.Payment;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface PaymentRepositoryCustom {

  Page<Payment> findBySearchOption(Pageable pageable, String userId, String paymentKey,
      String paymentId, String orderId, String state);

}
