package com.sparta.slack.application.dto;

import lombok.Getter;
import lombok.Setter;

public class MessageResponse {

  @Getter
  @Setter
  public static class Create {

    private Long userId;
    private String receiverEmail;
    private String message;

    public Create(MessageRequest.Create messageRequest) {
      this.userId = messageRequest.getUserId();
      this.receiverEmail = messageRequest.getReceiverEmail();
      this.message = messageRequest.getMessage();
    }

  }

}
