package com.sparta.order.server.domain.model;

import com.sparta.common.domain.entity.BaseEntity;
import com.sparta.order.server.domain.model.vo.OrderState;
import com.sparta.order.server.domain.model.vo.OrderType;
import com.sparta.order.server.presentation.dto.OrderDto.OrderCreateRequest;
import com.sparta.order.server.presentation.dto.OrderDto.OrderProductInfo;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import jakarta.persistence.UniqueConstraint;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Random;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Getter
@Builder(access = AccessLevel.PROTECTED)
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor(access = AccessLevel.PROTECTED)
@Table(name = "P_ORDER",
    uniqueConstraints = @UniqueConstraint(columnNames = "order_no", name = "UK_ORDER_NO"))
public class Order extends BaseEntity {

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long orderId;

  @Column(nullable = false)
  private Long userId;

  @Column(nullable = false)
  private Long paymentId;

  @Column(nullable = false, unique = true)
  private String orderNo;

  @Column(nullable = false)
  @Enumerated(value = EnumType.STRING)
  private OrderType type;

  @Column(nullable = false)
  @Enumerated(value = EnumType.STRING)
  private OrderState state;

  @Column(nullable = false)
  private Integer totalQuantity;

  @Column(nullable = false)
  private float totalAmount;

  @Column(nullable = false)
  private float shippingAmount;

  @Column(nullable = false)
  private float totalRealAmount;

  @Column(nullable = false, columnDefinition = "int default 0")
  private Integer pointPrice;

  @Column(nullable = false)
  private Integer couponPrice;

  @Column(nullable = true)
  private String invoiceNumber;

  @Column(nullable = false)
  private String recipient;

  @Column(nullable = false)
  private String phoneNumber;

  @Column(nullable = false)
  private String zipcode;

  @Column(nullable = false)
  private String shippingAddress;

  // TODO AddressDto 추가
  public static Order createOrder(Long userId, OrderCreateRequest request, int couponPrice) {

    final PriceInfo priceInfo = calculatePrice(request, couponPrice);

    // order no 생성
    final String orderNo = generateOrderNo();

    return Order.builder()
        .userId(userId)
        .paymentId(1L)
        .orderNo(orderNo)
        .type(OrderType.valueOf(request.getOrderType()))
        .state(OrderState.COMPLETED)
        .totalQuantity(priceInfo.totalQuantity)
        .totalAmount(priceInfo.totalAmount)
        .shippingAmount(priceInfo.shippingAmount)
        .totalRealAmount(priceInfo.totalRealAmount)
        .pointPrice(request.getPointPrice())
        .couponPrice(couponPrice)
        .recipient("수령인")
        .phoneNumber("전화번호")
        .zipcode("우편번호")
        .shippingAddress("배송지")
        .build();
  }

  // TODO 상품 가격 제대로 변경
  private static PriceInfo calculatePrice(OrderCreateRequest request, int couponPrice) {
    final int totalQuantity = request.getOrderProductInfos().stream()
        .mapToInt((OrderProductInfo::getQuantity)).sum();
    final int totalProductAmount = request.getOrderProductInfos().stream()
        .mapToInt(orderProductInfo -> orderProductInfo.getQuantity() * 1000)
        .sum();
    final int shippingAmount = totalProductAmount >= 20000 ? 0 : 3000;
    final int totalAmount = totalProductAmount + shippingAmount;
    final int totalRealAmount = totalAmount - request.getPointPrice() - couponPrice;

    return new PriceInfo(totalQuantity, totalAmount, shippingAmount, totalRealAmount);
  }

  private static String generateOrderNo() {
    SimpleDateFormat dateFormat = new SimpleDateFormat("yyyyMMddHHmmss");
    String currentTime = dateFormat.format(new Date());
    String randomNumber = generateRandomNumber();
    return currentTime + randomNumber;
  }

  private static String generateRandomNumber() {
    StringBuilder sb = new StringBuilder();
    Random random = new Random();
    for (int i = 0; i < 6; i++) {
      int randomNumber = random.nextInt(10);
      sb.append(randomNumber);
    }
    return sb.toString();
  }

  @AllArgsConstructor
  private static class PriceInfo {

    private final int totalQuantity;
    private final int totalAmount;
    private final int shippingAmount;
    private final int totalRealAmount;

  }

}
