package com.sparta.promotion.server.presentation.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import java.time.LocalDateTime;
import lombok.Getter;
import lombok.Setter;

public class EventRequest {

  @Getter
  @Setter
  public static class Create {

    @NotBlank(message = "이벤트 제목은 필수입니다")
    @Size(max = 100)
    private String title;
    private String imgUrl;
    @NotBlank(message = "이벤트 내용은 필수입니다")
    private String content;
    @NotBlank(message = "이벤트 시작일은 필수입니다")
    private LocalDateTime startAt;
    @NotBlank(message = "이벤트 종료일은 필수입니다")
    private LocalDateTime endAt;

  }

  @Getter
  @Setter
  public static class Update {

    @NotBlank(message = "이벤트 제목은 필수입니다")
    @Size(max = 100)
    private String title;
    private String imgUrl;
    @NotBlank(message = "이벤트 내용은 필수입니다")
    private String content;
    @NotBlank(message = "이벤트 시작일은 필수입니다")
    private LocalDateTime startAt;
    @NotBlank(message = "이벤트 종료일은 필수입니다")
    private LocalDateTime endAt;

  }

}
