package com.sparta.payment.presentation;

import com.sparta.payment.application.service.PaymentService;
import com.sparta.payment.presentation.dto.PaymentRequest;
import com.sparta.payment.presentation.dto.PaymentResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/internal/payments")
@RequiredArgsConstructor
public class PaymentInternalController {

  private final PaymentService paymentService;

  @PostMapping("")
  public PaymentResponse.Create createPayment(@RequestBody PaymentRequest.Create createRequest) {
    return paymentService.createPayment(createRequest);
  }

  @PostMapping("/cancel")
  public void cancelPayment(@RequestBody PaymentRequest.Cancel cancelRequest) {
    paymentService.cancelPayment(cancelRequest);
  }

  @GetMapping("/{orderId}")
  public PaymentResponse.Get getPaymentAndHistoryByOrderId(@PathVariable Long orderId) {
    return paymentService.getPaymentAndHistoryByOrderId(orderId);
  }

}
