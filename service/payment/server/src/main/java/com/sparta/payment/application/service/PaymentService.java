package com.sparta.payment.application.service;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.sparta.payment.domain.entity.Payment;
import com.sparta.payment.domain.entity.PaymentHistory;
import com.sparta.payment.domain.entity.PaymentState;
import com.sparta.payment.domain.repository.PaymentHistoryRepository;
import com.sparta.payment.domain.repository.PaymentRepository;
import com.sparta.payment.exception.PaymentErrorCode;
import com.sparta.payment.exception.PaymentException;
import com.sparta.payment.infrastructure.client.MessageClient;
import com.sparta.payment.infrastructure.event.PaymentCompletedEvent;
import com.sparta.payment.presentation.dto.PaymentHistoryResponse;
import com.sparta.payment.presentation.dto.PaymentRequest;
import com.sparta.payment.presentation.dto.PaymentRequest.Create;
import com.sparta.payment.presentation.dto.PaymentResponse;
import com.sparta.payment.presentation.dto.PaymentResponse.Get;
import com.sparta.slack_dto.infrastructure.MessageInternalDto;
import java.util.Base64;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.client.RestTemplate;

@Service
@Slf4j
public class PaymentService {

  @Value("${TOSS_SECRET_KEY}")
  private String originalKey;

  @Value("${PAYMENT.SUCCESS_URL}")
  private String SUCCESS_URL;

  @Value("${PAYMENT.FAIL_URL}")
  private String FAIL_URL;
  private final String tossPaymentsUrl = "https://api.tosspayments.com/v1/payments";

  private final KafkaTemplate<String, Object> kafkaTemplate;

  private final PaymentRepository paymentRepository;
  private final PaymentHistoryRepository paymentHistoryRepository;
  private final RestTemplate restTemplate;
  private final MessageClient messageClient;

  public PaymentService(KafkaTemplate<String, Object> kafkaTemplate,
      PaymentRepository paymentRepository,
      PaymentHistoryRepository paymentHistoryRepository,
      RestTemplateBuilder restTemplateBuilder, MessageClient messageClient) {
    this.kafkaTemplate = kafkaTemplate;
    this.paymentRepository = paymentRepository;
    this.paymentHistoryRepository = paymentHistoryRepository;
    this.restTemplate = restTemplateBuilder.build();
    this.messageClient = messageClient;
  }


  public void createPayment(Create request) {
    String secretKey = Base64.getEncoder().encodeToString(originalKey.getBytes());
    try {
      HttpHeaders headers = new HttpHeaders();
      headers.setBasicAuth(secretKey);

      PaymentRequest.CreateExt body = new PaymentRequest.CreateExt();
      body.setOrderId(request.getOrderId());
      body.setOrderName(request.getOrderName());
      body.setAmount(request.getAmount());
      body.setSuccessUrl(SUCCESS_URL);
      body.setFailUrl(FAIL_URL);

      HttpEntity<PaymentRequest.CreateExt> entity = new HttpEntity<>(body, headers);

      ResponseEntity<PaymentResponse.Create> response = restTemplate.exchange(
          tossPaymentsUrl, HttpMethod.POST, entity, PaymentResponse.Create.class
      );

      Payment payment = Payment.create(request);
      payment.setPaymentKey(Objects.requireNonNull(response.getBody()).getPaymentKey());

      sendMessage(response.getBody().getCheckout(), request.getEmail());

      paymentRepository.save(payment);
    } catch (Exception e) {
      log.error(e.getMessage());
      throw new PaymentException(PaymentErrorCode.INVALID_PARAMETER);
    }
  }

  private void sendMessage(Object checkout, String usermail) {
    try {
      String checkoutUrl = checkout.toString().replace("{url=", "").replace("}", "");

      MessageInternalDto.Create messageRequest = new MessageInternalDto.Create();
      messageRequest.setReceiverEmail(usermail);
      messageRequest.setMessage(checkoutUrl);

      messageClient.sendMessage(messageRequest);
    } catch (Exception e) {
      log.error(e.getMessage());
      throw new PaymentException(PaymentErrorCode.INVALID_PARAMETER);
    }
  }


  @Transactional
  public Payment paymentSuccess(String paymentKey) {
    Payment payment = paymentRepository.findByPaymentKey(paymentKey);
    if (payment == null) {
      throw new PaymentException(PaymentErrorCode.PAYMENT_NOT_FOUND);
    }

    HttpHeaders headers = new HttpHeaders();
    headers.setBasicAuth(Base64.getEncoder().encodeToString(originalKey.getBytes()));

    PaymentRequest.Confirm body = new PaymentRequest.Confirm();
    body.setPaymentKey(paymentKey);
    body.setOrderId(payment.getOrderId());
    body.setAmount(payment.getAmount());

    HttpEntity<PaymentRequest.Confirm> entity = new HttpEntity<>(body, headers);

    restTemplate.exchange(
        tossPaymentsUrl + "/confirm", HttpMethod.POST, entity,
        Object.class
    );

    PaymentCompletedEvent event = PaymentCompletedEvent.builder()
        .paymentId(payment.getPaymentId())
        .orderId(payment.getOrderId())
        .amount(payment.getAmount())
        .userId(payment.getUserId())
        .success(true)
        .build();

    try {
      ObjectMapper objectMapper = new ObjectMapper();
      String jsonMessage = objectMapper.writeValueAsString(event);
      kafkaTemplate.send("payment-completed-topic", jsonMessage);

    } catch (Exception e) {
      log.error(e.getMessage());
      throw new PaymentException(PaymentErrorCode.INVALID_PARAMETER);
    }

    payment.setState(PaymentState.PAYMENT);
    PaymentHistory history = PaymentHistory.create(payment);
    paymentHistoryRepository.save(history);

    return payment;
  }

  public void paymentFail(String paymentKey) {
    Payment payment = paymentRepository.findByPaymentKey(paymentKey);
    if (payment == null) {
      throw new PaymentException(PaymentErrorCode.INVALID_PARAMETER);
    }

    PaymentCompletedEvent event = PaymentCompletedEvent.builder()
        .paymentId(payment.getPaymentId())
        .orderId(payment.getOrderId())
        .amount(payment.getAmount())
        .success(false)
        .build();

    kafkaTemplate.send("payment-completed-topic", event);

    payment.setState(PaymentState.CANCEL);
    paymentRepository.save(payment);
  }

  public void cancelPayment(PaymentRequest.Cancel cancelRequest) {
    Payment payment = paymentRepository.findByOrderId(cancelRequest.getOrderId());
    if (payment == null) {
      throw new PaymentException(PaymentErrorCode.PAYMENT_NOT_FOUND);
    }

    try {
      String secretKey = Base64.getEncoder().encodeToString(originalKey.getBytes());

      HttpHeaders headers = new HttpHeaders();
      headers.setBasicAuth(secretKey);

      PaymentRequest.Cancel body = new PaymentRequest.Cancel();
      body.setCancelReason(cancelRequest.getCancelReason());

      HttpEntity<PaymentRequest.Cancel> entity = new HttpEntity<>(body, headers);

      restTemplate.exchange(
          tossPaymentsUrl + "/" + payment.getPaymentKey() + "/cancel", HttpMethod.POST, entity,
          Object.class
      );

      PaymentHistory history = PaymentHistory.cancel(payment, cancelRequest.getCancelReason());
      paymentHistoryRepository.save(history);

      payment.setState(PaymentState.CANCEL);
      paymentRepository.save(payment);

    } catch (Exception e) {
      log.error(e.getMessage());
      throw new PaymentException(PaymentErrorCode.INVALID_PARAMETER);
    }

  }


  public PaymentResponse.GetByOrderId getPaymentByOrderId(Long paymentId) {
    Payment payment = paymentRepository.findByPaymentId(paymentId);
    if (payment == null) {
      throw new PaymentException(PaymentErrorCode.PAYMENT_NOT_FOUND);
    }

    PaymentResponse.GetByOrderId response = new PaymentResponse.GetByOrderId();
    response.setPaymentId(payment.getPaymentId());
    response.setOrderId(payment.getOrderId());
    response.setOrderName(payment.getOrderName());
    response.setState(payment.getState());
    response.setAmount(payment.getAmount());
    response.setCreatedAt(payment.getCreatedAt());

    return response;
  }


  public Page<PaymentResponse.Get> getAllPayments(Pageable pageable, String userId,
      String paymentKey,
      String paymentId, String orderId, String state) {
    Page<Payment> payments = paymentRepository.findBySearchOption(pageable, userId, paymentKey,
        paymentId, orderId, state);
    return payments.map(this::convertToPaymentDto);
  }

  private Get convertToPaymentDto(Payment payment) {
    Get response = new Get();
    response.setPaymentId(payment.getPaymentId());
    response.setOrderId(payment.getOrderId());
    response.setState(payment.getState());
    response.setOrderName(payment.getOrderName());
    response.setAmount(payment.getAmount());
    response.setCreatedAt(payment.getCreatedAt());

    return response;
  }

  public List<PaymentHistoryResponse.Get> getPaymentHistory(Long paymentId) {
    Payment payment = paymentRepository.findById(paymentId)
        .orElseThrow(() -> new PaymentException(PaymentErrorCode.PAYMENT_NOT_FOUND));

    List<PaymentHistory> histories = paymentHistoryRepository.findByPayment_PaymentId(paymentId);
    return histories.stream()
        .map(history -> PaymentHistoryResponse.Get.builder()
            .amount(history.getAmount())
            .type(history.getType())
            .cancelReason(history.getCancelReason())
            .createdAt(history.getCreatedAt())
            .build())
        .toList();
  }

  public PaymentResponse.Get getPaymentAndHistoryByOrderId(Long orderId) {
    Payment payment = paymentRepository.findByOrderId(orderId);
    if (payment == null) {
      throw new PaymentException(PaymentErrorCode.PAYMENT_NOT_FOUND);
    }
    List<PaymentHistory> histories = paymentHistoryRepository.findByPayment_PaymentId(
        payment.getPaymentId());

    PaymentResponse.Get response = new PaymentResponse.Get();
    response.setPaymentId(payment.getPaymentId());
    response.setOrderId(payment.getOrderId());
    response.setState(payment.getState());
    response.setAmount(payment.getAmount());
    response.setCreatedAt(payment.getCreatedAt());

    response.setHistories(histories.stream()
        .map(this::convertToHistoryDto)
        .collect(Collectors.toList()));

    return response;
  }

  private PaymentResponse.PaymentHistoryDto convertToHistoryDto(PaymentHistory history) {
    PaymentResponse.PaymentHistoryDto dto = new PaymentResponse.PaymentHistoryDto();
    dto.setAmount(history.getAmount());
    dto.setType(history.getType());
    dto.setCancelReason(history.getCancelReason());
    dto.setCreatedAt(history.getCreatedAt());

    return dto;
  }


}
