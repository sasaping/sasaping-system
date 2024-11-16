package dto;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
public class NotificationOrderDto {

  private Long orderId;
  private Long userId;
  private String orderState;
  private String displayProductName;
  private Integer totalQuantity;

}
