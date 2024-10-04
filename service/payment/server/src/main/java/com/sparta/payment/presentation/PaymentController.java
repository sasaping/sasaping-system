package com.sparta.payment.presentation;

import com.sparta.payment.application.dto.PaymentRequest;
import com.sparta.payment.application.dto.PaymentResponse;
import com.sparta.payment.application.service.PaymentService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/internal/payments")
@Slf4j
@RequiredArgsConstructor
public class PaymentController {

  private final PaymentService paymentService;

  @PostMapping("")
  public PaymentResponse.Create createPayment(@RequestBody PaymentRequest.Create createRequest) {
    return paymentService.createPayment(createRequest);
  }

}
