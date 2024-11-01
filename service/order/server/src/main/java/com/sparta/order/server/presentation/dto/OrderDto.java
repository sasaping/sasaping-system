package com.sparta.order.server.presentation.dto;

import com.sparta.order.server.domain.model.Order;
import com.sparta.order.server.domain.model.OrderProduct;
import com.sparta.payment_dto.infrastructure.PaymentInternalDto;
import java.math.BigDecimal;
import java.time.LocalDateTime;
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
    private LocalDateTime orderDate;

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
          payment,
          order.getCreatedAt());
    }

  }

  @Getter
  @NoArgsConstructor
  @AllArgsConstructor
  public static class OrderProductResponse {

    private Long orderProductId;
    private String productId;
    private String productName;
    private Integer quantity;
    private BigDecimal purchasePrice;
    private BigDecimal couponPrice;

    public static OrderProductResponse fromEntity(OrderProduct orderProduct) {
      return new OrderProductResponse(
          orderProduct.getOrderProductId(),
          orderProduct.getProductId(),
          orderProduct.getProductName(),
          orderProduct.getQuantity(),
          orderProduct.getPurchasePrice(),
          orderProduct.getCouponPrice()
      );
    }

  }

  @Getter
  @NoArgsConstructor
  @AllArgsConstructor
  public static class MyOrderGetResponse {

    private Long orderId;
    private Long userId;
    private String orderNo;
    private String orderType;
    private String orderState;
    private List<MyOrderProductResponse> myOrderProducts = new ArrayList<>();
    private Integer totalQuantity;
    private String invoiceNumber;
    private LocalDateTime orderDate;

    public static MyOrderGetResponse from(Order order,
        List<MyOrderProductResponse> myOrderProductResponses) {
      return new MyOrderGetResponse(
          order.getOrderId(),
          order.getUserId(),
          order.getOrderNo(),
          order.getType().getDescription(),
          order.getState().getDescription(),
          myOrderProductResponses,
          order.getTotalQuantity(),
          order.getInvoiceNumber(),
          order.getCreatedAt());
    }

  }

  @Getter
  @NoArgsConstructor
  @AllArgsConstructor
  public static class AllOrderGetResponse {

    private Long orderId;
    private Long userId;
    private String orderNo;
    private String orderType;
    private String orderState;
    private List<String> productIds = new ArrayList<>();
    private Integer totalQuantity;
    private String invoiceNumber;
    private LocalDateTime orderDate;

    public static AllOrderGetResponse from(Order order) {
      return new AllOrderGetResponse(
          order.getOrderId(),
          order.getUserId(),
          order.getOrderNo(),
          order.getType().getDescription(),
          order.getState().getDescription(),
          order.getOrderProducts().stream().map(orderProduct -> orderProduct.getProductId())
              .toList(),
          order.getTotalQuantity(),
          order.getInvoiceNumber(),
          order.getCreatedAt());
    }

  }

  @Getter
  @NoArgsConstructor
  @AllArgsConstructor
  public static class MyOrderProductResponse {

    private Long orderProductId;
    private String productId;
    private String productName;
    private Integer quantity;
    private BigDecimal purchasePrice;

    public static MyOrderProductResponse fromEntity(OrderProduct orderProduct) {
      return new MyOrderProductResponse(
          orderProduct.getOrderProductId(),
          orderProduct.getProductId(),
          orderProduct.getProductName(),
          orderProduct.getQuantity(),
          orderProduct.getPurchasePrice()
      );
    }

  }


}
