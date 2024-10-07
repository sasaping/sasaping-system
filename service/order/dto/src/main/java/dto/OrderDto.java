package dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@AllArgsConstructor
@Builder
@NoArgsConstructor
public class OrderDto {

  private Long id;
  private Long userId;
  private Long paymentId;
  private String orderNo;
  private String type;
  private String state;
  private int totalQuantity;
  private int totalAmount;
  private int shippingAmount;
  private int totalRealAmount;
  private int pointPrice;
  private int couponPrice;
  private String invoiceNumber;
  private String recipient;
  private String phoneNumber;
  private String zipcode;
  private String shippingAddress;
}
