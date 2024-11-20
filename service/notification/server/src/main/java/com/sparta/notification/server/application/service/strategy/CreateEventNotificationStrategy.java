package com.sparta.notification.server.application.service.strategy;

import com.sparta.notification.server.domain.model.vo.NotificationType;
import dto.NotificationCreateRequest;
import org.springframework.stereotype.Service;

@Service
public class CreateEventNotificationStrategy implements CreateNotificationService {

  @Override
  public Long createNotification(NotificationCreateRequest request) {

    return null;
  }

  @Override
  public boolean isMatched(NotificationType notificationType) {
    return notificationType.isEvent();
  }

}
