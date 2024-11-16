package dto;

import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
@AllArgsConstructor
public class NotificationCreateRequest {

  @NotNull(message = "사용자 ID는 필수 값입니다.")
  private Long userId;

  private Long targetId;

  @NotNull(message = "알림 타입은 필수 값입니다.")
  private String notificationType;

}
