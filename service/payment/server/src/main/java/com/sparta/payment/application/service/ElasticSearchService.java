package com.sparta.payment.application.service;

import com.sparta.payment.domain.entity.ElasticSearchPayment;
import com.sparta.payment.domain.entity.Payment;
import com.sparta.payment.exception.PaymentErrorCode;
import com.sparta.payment.exception.PaymentException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.elasticsearch.core.ElasticsearchOperations;
import org.springframework.stereotype.Service;

@Service
@Slf4j
public class ElasticSearchService {

  @Autowired
  private ElasticsearchOperations elasticsearchOperations;

  public void savePayment(Payment payment) {
    ElasticSearchPayment esPayment = convertToElasticsearchPayment(payment);
    try {
      elasticsearchOperations.save(esPayment);
    } catch (Exception e) {
      log.error(e.getMessage());
      throw new PaymentException(PaymentErrorCode.INVALID_PARAMETER);
    }
  }

  private ElasticSearchPayment convertToElasticsearchPayment(Payment payment) {
    return ElasticSearchPayment.builder()
        .paymentId((payment.getPaymentId().toString()))
        .userId(payment.getUserId().toString())
        .orderName(payment.getOrderName())
        .orderId(payment.getOrderId().toString())
        .createdAt(payment.getCreatedAt())
        .amount(payment.getAmount())
        .build();
  }

}
