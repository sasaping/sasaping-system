package com.sparta.order.server.domain.model;

import com.sparta.common.domain.entity.BaseEntity;
import com.sparta.order.server.domain.model.vo.OrderState;
import com.sparta.order.server.domain.model.vo.OrderType;
import com.sparta.order.server.exception.OrderErrorCode;
import com.sparta.order.server.exception.OrderException;
import com.sparta.product_dto.ProductDto;
import com.sparta.user.user_dto.infrastructure.AddressDto;
import dto.OrderCreateRequest;
import dto.OrderProductInfo;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import jakarta.persistence.UniqueConstraint;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.Map;
import java.util.Random;
import java.util.stream.Collectors;
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

  private static final BigDecimal FREE_SHIPPING_THRESHOLD = new BigDecimal("20000");
  private static final BigDecimal SHIPPING_AMOUNT = new BigDecimal("3000");

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long orderId;

  @Column(nullable = false)
  private Long userId;

  @Column(nullable = false)
  private Long paymentId;

  @Column(nullable = false, unique = true, length = 50)
  private String orderNo;

  @Column(nullable = false, length = 50)
  @Enumerated(value = EnumType.STRING)
  private OrderType type;

  @Column(nullable = false, length = 50)
  @Enumerated(value = EnumType.STRING)
  private OrderState state;

  @Column(nullable = false)
  private Integer totalQuantity;

  @Column(nullable = false)
  private BigDecimal totalAmount;

  @Column(nullable = false)
  private BigDecimal shippingAmount;

  @Column(nullable = false)
  private BigDecimal totalRealAmount;

  @Column(nullable = false, columnDefinition = "int default 0")
  private BigDecimal pointPrice;

  @Column(nullable = false)
  private BigDecimal couponPrice;

  @Column(nullable = true, length = 255)
  private String invoiceNumber;

  @Column(nullable = false, length = 100)
  private String recipient;

  @Column(nullable = false, length = 100)
  private String phoneNumber;

  @Column(nullable = false, length = 255)
  private String zipcode;

  @Column(nullable = false, length = 255)
  private String shippingAddress;

  public void cancel() {
    state = OrderState.CANCELED;
  }

  public void complete() {
    state = OrderState.COMPLETED;
  }

  public void setPaymentId(Long paymentId) {
    this.paymentId = paymentId;
  }

  // TODO AddressDto 추가
  public static Order createOrder(Long userId, OrderCreateRequest request,
      List<ProductDto> products, BigDecimal couponPrice, AddressDto address) {

    final PriceInfo priceInfo = calculatePrice(request, products, couponPrice);

    final String orderNo = generateOrderNo(OrderType.valueOf(request.getOrderType()));

    return Order.builder()
        .userId(userId)
        .paymentId(1L)
        .orderNo(orderNo)
        .type(OrderType.valueOf(request.getOrderType()))
        .state(OrderState.PENDING_PAYMENT)
        .totalQuantity(priceInfo.totalQuantity)
        .totalAmount(priceInfo.totalAmount)
        .shippingAmount(priceInfo.shippingAmount)
        .totalRealAmount(priceInfo.totalRealAmount)
        .pointPrice(request.getPointPrice())
        .couponPrice(couponPrice)
        .recipient(address.getRecipient())
        .phoneNumber(address.getPhoneNumber())
        .zipcode(address.getZipcode())
        .shippingAddress(address.getAddress())
        .build();
  }

  private static PriceInfo calculatePrice(OrderCreateRequest request, List<ProductDto> products,
      BigDecimal couponPrice) {
    Map<String, BigDecimal> productPrices = products.stream().collect(
        Collectors.toMap(
            product -> product.getProductId().toString(),
            ProductDto::getDiscountedPrice)
    );

    final int totalQuantity = request.getOrderProductInfos().stream()
        .mapToInt((OrderProductInfo::getQuantity)).sum();

    final BigDecimal totalProductAmount = request.getOrderProductInfos().stream()
        .map(orderProductInfo -> BigDecimal.valueOf(orderProductInfo.getQuantity())
            .multiply(productPrices.get(orderProductInfo.getProductId())))
        .reduce(BigDecimal.ZERO, BigDecimal::add);

    final BigDecimal shippingAmount = totalProductAmount.compareTo(FREE_SHIPPING_THRESHOLD) >= 0
        ? BigDecimal.ZERO
        : SHIPPING_AMOUNT;

    final BigDecimal totalAmount = totalProductAmount.add(shippingAmount);

    final BigDecimal totalRealAmount = totalAmount
        .subtract(request.getPointPrice())
        .subtract(couponPrice);

    return new PriceInfo(totalQuantity, totalAmount, shippingAmount, totalRealAmount);
  }

  private static String generateOrderNo(OrderType orderType) {
    String orderTypePrefix = switch (orderType) {
      case PREORDER -> OrderType.PREORDER.getPrefix();
      case RAFFLE -> OrderType.RAFFLE.getPrefix();
      case STANDARD -> OrderType.STANDARD.getPrefix();
      default -> throw new OrderException(OrderErrorCode.ORDER_NO_GENERATION_FAILED);
    };
    DateTimeFormatter dateFormat = DateTimeFormatter.ofPattern("yyyyMMddHHmmss");
    String currentTime = LocalDateTime.now().format(dateFormat);
    String randomNumber = generateRandomNumber();
    return orderTypePrefix + currentTime + randomNumber;
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

  public void validateOrderPermission(Long userId) {
    if (!this.userId.equals(userId)) {
      throw new OrderException(OrderErrorCode.ORDER_PERMISSION_DENIED);
    }
  }

  public void validateOrderCancel() {
    if (!state.equals(OrderState.PENDING_PAYMENT)
        && !state.equals(OrderState.COMPLETED)) {
      throw new OrderException(OrderErrorCode.CANNOT_CANCEL_WHILE_SHIPPING, orderId);
    }
  }

  @AllArgsConstructor
  private static class PriceInfo {

    private final int totalQuantity;
    private final BigDecimal totalAmount;
    private final BigDecimal shippingAmount;
    private final BigDecimal totalRealAmount;

  }


}
