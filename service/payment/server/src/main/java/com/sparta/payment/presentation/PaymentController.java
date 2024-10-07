package com.sparta.payment.presentation;

import com.sparta.payment.application.dto.PaymentRequest;
import com.sparta.payment.application.dto.PaymentResponse;
import com.sparta.payment.application.service.PaymentService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@Slf4j
@RequiredArgsConstructor
public class PaymentController {

  private final PaymentService paymentService;

  @PostMapping("/internal/payments")
  public PaymentResponse.Create createPayment(@RequestBody PaymentRequest.Create createRequest) {
    return paymentService.createPayment(createRequest);
  }

  @GetMapping("/payments/success")
  public void paymentSuccess(@RequestParam String paymentKey) {
    paymentService.paymentSuccess(paymentKey);
  }


  @GetMapping("/payments/fail")
  public void paymentFail(@RequestParam String paymentKey) {
    paymentService.paymentFail(paymentKey);
  }

  @PostMapping("/internal/payments/cancel")
  public void cancelPayment(@RequestBody PaymentRequest.Cancel cancelRequest) {
    paymentService.cancelPayment(cancelRequest);
  }

  // order Id로 결제 내역 조회
  // paymentId로 결제 내역 조회 - history 있으면 같이 조회
  // manager 가 조회하는 결제내역

  @GetMapping("/internal/payments/{orderId}")
  public PaymentResponse.Get getPaymentByOrderId(@PathVariable Long orderId) {
    return paymentService.getPaymentHistories(orderId);
  }


}
