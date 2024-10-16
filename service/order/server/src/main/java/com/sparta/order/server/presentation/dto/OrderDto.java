package com.sparta.order.server.presentation.dto;

import com.sparta.order.server.domain.model.Order;
import com.sparta.order.server.domain.model.OrderProduct;
import com.sparta.payment_dto.infrastructure.PaymentInternalDto;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

public class OrderDto {

  @Getter
  @NoArgsConstructor
  @AllArgsConstructor
  public static class OrderGetResponse {

    private Long orderId;
    private Long userId;
    private String orderNo;
    private String orderType;
    private String orderState;
    private List<OrderProductResponse> orderProducts = new ArrayList<>();
    private Integer totalQuantity;
    private BigDecimal totalAmount;
    private BigDecimal shippingAmount;
    private BigDecimal totalRealAmount;
    private BigDecimal pointPrice;
    private BigDecimal couponPrice;
    private String invoiceNumber;
    private String recipient;
    private String phoneNumber;
    private String zipcode;
    private String shippingAddress;
    private Long paymentId;
    private PaymentInternalDto.Get payment;

    public static OrderDto.OrderGetResponse from(Order order,
        List<OrderProductResponse> orderProductResponses,
        PaymentInternalDto.Get payment) {
      return new OrderGetResponse(
          order.getOrderId(),
          order.getUserId(),
          order.getOrderNo(),
          order.getType().getDescription(),
          order.getState().getDescription(),
          orderProductResponses,
          order.getTotalQuantity(),
          order.getTotalAmount(),
          order.getShippingAmount(),
          order.getTotalRealAmount(),
          order.getPointPrice(),
          order.getCouponPrice(),
          order.getInvoiceNumber(),
          order.getRecipient(),
          order.getPhoneNumber(),
          order.getZipcode(),
          order.getShippingAddress(),
          order.getPaymentId(),
          payment);
    }

  }

  @Getter
  @NoArgsConstructor
  @AllArgsConstructor
  public static class OrderProductResponse {

    private Long productId;
    private String productName;
    private Integer quantity;
    private BigDecimal purchasePrice;
    private BigDecimal couponPrice;

    public static OrderProductResponse fromEntity(OrderProduct orderProduct) {
      return new OrderProductResponse(
          orderProduct.getOrderProductId(),
          orderProduct.getProductName(),
          orderProduct.getQuantity(),
          orderProduct.getPurchasePrice(),
          orderProduct.getCouponPrice()
      );
    }

  }

}
