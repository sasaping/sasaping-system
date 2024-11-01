package com.sparta.order.server.infrastructure.client;

import com.sparta.payment_dto.infrastructure.PaymentInternalDto;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

@FeignClient(name = "payment")
public interface PaymentClient {

  @PostMapping("/internal/payments")
  void payment(@RequestBody PaymentInternalDto.Create createRequest);

  @PostMapping("/internal/payments/cancel")
  void cancelPayment(@RequestBody PaymentInternalDto.Cancel cancelRequest);

  @GetMapping("/internal/payments/{orderId}")
  PaymentInternalDto.Get getPayment(@PathVariable(name = "orderId") Long orderId);

}
