package dto;

import java.util.UUID;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class OrderProductDto {

  private Long orderProductId;
  private Long orderId;
  private UUID productId;
  private Integer quantity;
  private int purchasePrice;

}
