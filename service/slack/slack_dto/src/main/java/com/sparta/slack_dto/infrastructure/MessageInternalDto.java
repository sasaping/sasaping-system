package com.sparta.slack_dto.infrastructure;

import lombok.Getter;
import lombok.Setter;

public class MessageInternalDto {

  @Getter
  @Setter
  public static class Create {

    private String receiverEmail;
    private String message;
  }
}
