package com.sparta.payment.domain.repository;

import com.sparta.payment.domain.entity.PaymentHistory;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface PaymentHistoryRepository extends JpaRepository<PaymentHistory, Long> {

  List<PaymentHistory> findByPayment_PaymentId(Long paymentId);


}
