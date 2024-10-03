package com.sparta.slack.domain.entity;

import com.sparta.common.domain.entity.BaseEntity;
import com.sparta.slack.application.dto.MessageRequest;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@Builder(access = AccessLevel.PRIVATE)
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "p_message")
@Entity
public class Message extends BaseEntity {

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  @Column(name = "message_id")
  private Long messageId;

  @Column(name = "user_id")
  private Long userId;

  @Column(name = "receiver_email")
  private String receiverEmail;

  @Column(name = "message")
  private String message;

  public static Message create(MessageRequest.Create request) {
    return Message.builder()
        .userId(request.getUserId())
        .receiverEmail(request.getReceiverEmail())
        .message(request.getMessage())
        .build();
  }

}
