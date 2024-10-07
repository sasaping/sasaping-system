package com.sparta.slack.domain.entity;

import com.sparta.slack.application.dto.MessageRequest;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import java.time.LocalDateTime;
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
public class Message {

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  @Column(name = "message_id")
  private Long messageId;

  @Column(name = "receiver_email")
  private String receiverEmail;

  @Column(name = "message")
  private String message;

  @Column(name = "send_at")
  private LocalDateTime sendAt;

  public static Message create(MessageRequest.Create request) {
    return Message.builder()
        .receiverEmail(request.getReceiverEmail())
        .message(request.getMessage())
        .sendAt(LocalDateTime.now())
        .build();
  }
}
