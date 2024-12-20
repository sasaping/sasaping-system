package com.sparta.payment.presentation;

import com.sparta.common.domain.response.ApiResponse;
import com.sparta.payment.application.service.PaymentService;
import com.sparta.payment.domain.entity.Payment;
import com.sparta.payment.presentation.dto.PaymentHistoryResponse;
import com.sparta.payment.presentation.dto.PaymentResponse;
import java.util.List;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Pageable;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.thymeleaf.TemplateEngine;
import org.thymeleaf.context.Context;

@RestController
@Slf4j
@RequiredArgsConstructor
@Validated
public class PaymentController {

  private final PaymentService paymentService;
  private final TemplateEngine templateEngine;


  @GetMapping("/payments/success")
  public ResponseEntity<String> paymentSuccess(@RequestParam String paymentKey) {
    Payment payment = paymentService.paymentSuccess(paymentKey);
    Context context = new Context();
    context.setVariable("orderId", payment.getOrderId());
    context.setVariable("orderName", payment.getOrderName());
    context.setVariable("amount", payment.getAmount());
    context.setVariable("createdAt", payment.getCreatedAt());

    String htmlContent = templateEngine.process("success", context);

    return ResponseEntity.ok()
        .contentType(MediaType.TEXT_HTML)
        .body(htmlContent);
  }

  @GetMapping("/payments/fail")
  public void paymentFail(@RequestParam String paymentKey) {
    paymentService.paymentFail(paymentKey);
  }

  @GetMapping("/payments/{paymentId}")
  public PaymentResponse.GetByOrderId getPaymentByOrderId(@PathVariable Long paymentId) {
    return paymentService.getPaymentByOrderId(paymentId);
  }

  @GetMapping("/payments/{paymentId}/history")
  public List<PaymentHistoryResponse.Get> getPaymentHistory(@PathVariable Long paymentId) {
    return paymentService.getPaymentHistory(paymentId);
  }


  @PreAuthorize("hasAnyRole('ROLE_MANAGER', 'ROLE_ADMIN')")
  @GetMapping("/api/payments/all")
  public ApiResponse<?> getAllPayments(Pageable pageable,
      @RequestParam(required = false) String userId,
      @RequestParam(required = false) String paymentKey,
      @RequestParam(required = false) String paymentId,
      @RequestParam(required = false) String orderId,
      @RequestParam(required = false) String state) {
    return ApiResponse.ok(paymentService.getAllPayments(pageable, userId, paymentKey, paymentId,
        orderId, state));
  }

}
