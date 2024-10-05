package com.sparta.payment.application.service;

import com.sparta.payment.application.dto.PaymentRequest;
import com.sparta.payment.application.dto.PaymentRequest.Create;
import com.sparta.payment.application.dto.PaymentResponse;
import com.sparta.payment.domain.entity.Payment;
import com.sparta.payment.domain.repository.PaymentRepository;
import java.util.Base64;
import java.util.Objects;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.RestTemplate;

@Service
@Slf4j
public class PaymentService {

  @Value("${TOSS_SECRET_KEY}")
  private String originalKey;
  private final String tossPaymentsUrl = "https://api.tosspayments.com/v1/payments";

  private final PaymentRepository paymentRepository;
  private final RestTemplate restTemplate;

  public PaymentService(PaymentRepository paymentRepository,
      RestTemplateBuilder restTemplateBuilder) {
    this.paymentRepository = paymentRepository;
    this.restTemplate = restTemplateBuilder.build();
  }


  public PaymentResponse.Create createPayment(Create request) {
    String secretKey = Base64.getEncoder().encodeToString(originalKey.getBytes());
    try {
      HttpHeaders headers = new HttpHeaders();
      headers.setBasicAuth(secretKey);

      PaymentRequest.CreateExt body = new PaymentRequest.CreateExt();
      body.setOrderId(request.getOrderId());
      body.setOrderName(request.getOrderName());
      body.setAmount(request.getAmount());

      HttpEntity<PaymentRequest.Create> entity = new HttpEntity<>(body, headers);

      ResponseEntity<PaymentResponse.Create> response = restTemplate.exchange(
          tossPaymentsUrl, HttpMethod.POST, entity, PaymentResponse.Create.class
      );

      sendPaymentUrltoUser(request.getOrderId(), request.getOrderName(), response.getBody());

      Payment payment = Payment.create(request);
      payment.setPaymentKey(Objects.requireNonNull(response.getBody()).getPaymentKey());
      paymentRepository.save(payment);

      if (response.getStatusCode() == HttpStatus.OK && response.getBody() != null) {
        return response.getBody();
      } else {
        throw new RuntimeException("Failed to create payment");
      }
    } catch (HttpClientErrorException e) {
      throw new RuntimeException("Error occurred while creating payment: " + e.getMessage());
    }
  }

  // TODO : 슬랙 전송 feign 연결
  private void sendPaymentUrltoUser(Long orderId, String orderName, Object checkout) {
    log.info("orderId={}, orderName={}, checkout={}", orderId, orderName, checkout);
  }


}
