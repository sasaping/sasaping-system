package com.sparta.order.server.domain.model;

import com.sparta.common.domain.entity.BaseEntity;
import com.sparta.order.server.domain.model.vo.OrderState;
import com.sparta.order.server.domain.model.vo.OrderType;
import com.sparta.order.server.exception.OrderErrorCode;
import com.sparta.order.server.exception.OrderException;
import com.sparta.product_dto.ProductDto;
import com.sparta.user.user_dto.infrastructure.AddressDto;
import com.sparta.user.user_dto.infrastructure.UserDto;
import com.sparta.user.user_dto.infrastructure.UserDto.UserRole;
import dto.OrderCreateRequest;
import dto.OrderProductInfo;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;
import jakarta.persistence.UniqueConstraint;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Random;
import java.util.stream.Collectors;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.SQLRestriction;

@Entity
@Getter
@Builder(access = AccessLevel.PROTECTED)
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor(access = AccessLevel.PROTECTED)
@Table(name = "P_ORDER",
    uniqueConstraints = @UniqueConstraint(columnNames = "order_no", name = "UK_ORDER_NO"))
@SQLRestriction("is_deleted = false")
public class Order extends BaseEntity {

  private static final BigDecimal FREE_SHIPPING_THRESHOLD = new BigDecimal("20000");
  private static final BigDecimal SHIPPING_AMOUNT = new BigDecimal("3000");

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long orderId;

  @Column(nullable = false)
  private Long userId;

  @Column(nullable = true)
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

  @Column(nullable = false, columnDefinition = "int default 0")
  private BigDecimal couponPrice;

  @Column(nullable = true, length = 255)
  private String invoiceNumber;

  @Column(nullable = false, length = 100)
  private String addressAlias;

  @Column(nullable = false, length = 100)
  private String recipient;

  @Column(nullable = false, length = 100)
  private String phoneNumber;

  @Column(nullable = false, length = 255)
  private String zipcode;

  @Column(nullable = false, length = 255)
  private String shippingAddress;

  @Column(nullable = false)
  @Builder.Default
  private Boolean isDeleted = false;

  @OneToMany(mappedBy = "order")
  @Builder.Default
  private List<OrderProduct> orderProducts = new ArrayList<>();

  public void addOrderProduct(OrderProduct orderProduct) {
    orderProducts.add(orderProduct);
  }

  public void cancel() {
    state = OrderState.CANCELED;
  }

  public void complete() {
    state = OrderState.COMPLETED;
  }

  public void setPaymentId(Long paymentId) {
    this.paymentId = paymentId;
  }

  public void updateState(OrderState state) {
    this.state = state;
  }

  public void delete() {
    isDeleted = true;
  }

  public void updateAddress(AddressDto address) {
    this.addressAlias = address.getAlias();
    this.recipient = address.getRecipient();
    this.phoneNumber = address.getPhoneNumber();
    this.zipcode = address.getZipcode();
    this.shippingAddress = address.getAddress();
  }

  public void registerOrderInvoiceNumber(String invoiceNumber) {
    this.invoiceNumber = invoiceNumber;
    this.state = OrderState.SHIPPING;
  }


  public static Order createOrder(Long userId, OrderCreateRequest request,
      List<ProductDto> products, BigDecimal couponPrice, AddressDto address) {

    final PriceInfo priceInfo = calculatePrice(request, products, couponPrice);

    final String orderNo = generateOrderNo(OrderType.valueOf(request.getOrderType()));

    return Order.builder()
        .userId(userId)
        .orderNo(orderNo)
        .type(OrderType.valueOf(request.getOrderType()))
        .state(OrderState.PENDING_PAYMENT)
        .totalQuantity(priceInfo.totalQuantity)
        .totalAmount(priceInfo.totalAmount)
        .shippingAmount(priceInfo.shippingAmount)
        .totalRealAmount(priceInfo.totalRealAmount)
        .pointPrice(request.getPointPrice() != null ? request.getPointPrice() : BigDecimal.ZERO)
        .couponPrice(couponPrice != null ? couponPrice : BigDecimal.ZERO)
        .addressAlias(address.getAlias())
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
        .subtract(request.getPointPrice() != null ? request.getPointPrice() : BigDecimal.ZERO)
        .subtract(couponPrice != null ? couponPrice : BigDecimal.ZERO);

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

  public void validateOrderPermission(UserDto user) {
    if (!this.userId.equals(user.getUserId()) && user.getRole()
        .equals(UserRole.ROLE_USER.name())) {
      throw new OrderException(OrderErrorCode.ORDER_PERMISSION_DENIED);
    }
  }

  public void validateOrderUpdate() {
    if (!state.equals(OrderState.PENDING_PAYMENT)
        && !state.equals(OrderState.COMPLETED)) {
      throw new OrderException(OrderErrorCode.ORDER_CANNOT_CANCEL_WHILE_SHIPPING, orderId);
    }
  }

  public void validateOrderDelete() {
    if (!state.equals(OrderState.PURCHASE_CONFIRMED)) {
      throw new OrderException(OrderErrorCode.ORDER_CANNOT_DELETE, orderId);
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
