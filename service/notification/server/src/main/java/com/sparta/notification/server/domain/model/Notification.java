package com.sparta.notification.server.domain.model;

import com.sparta.common.domain.entity.BaseEntity;
import com.sparta.notification.server.domain.model.vo.NotificationType;
import dto.NotificationCreateRequest;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Table(name = "P_NOTIFICATION")
public class Notification extends BaseEntity {

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long notificationId;

  @Column(nullable = false)
  private Long userId;

  @Column(nullable = false)
  private Long targetId;

  @Column(nullable = false)
  @Enumerated(value = EnumType.STRING)
  private NotificationType type;

  @Column(nullable = false)
  private String message;

  @Column(nullable = false)
  private Boolean isRead = false;

  public static Notification create(NotificationCreateRequest dto, String message) {
    return new Notification(
        dto.getUserId(),
        dto.getTargetId(),
        NotificationType.valueOf(dto.getNotificationType()),
        message
    );
  }

  private Notification(Long userId, Long targetId, NotificationType type, String message) {
    this.userId = userId;
    this.targetId = targetId;
    this.type = type;
    this.message = message;
  }

}
