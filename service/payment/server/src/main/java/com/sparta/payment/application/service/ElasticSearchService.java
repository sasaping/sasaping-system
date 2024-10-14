package com.sparta.payment.application.service;

import com.sparta.payment.domain.entity.ElasticSearchPayment;
import com.sparta.payment.domain.entity.Payment;
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
    elasticsearchOperations.save(esPayment);

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
