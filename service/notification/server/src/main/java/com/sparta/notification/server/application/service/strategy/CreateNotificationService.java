package com.sparta.notification.server.application.service.strategy;

import com.sparta.notification.server.domain.model.vo.NotificationType;
import dto.NotificationCreateRequest;


public interface CreateNotificationService {

  Long createNotification(NotificationCreateRequest request);

  boolean isMatched(NotificationType notificationType);

}
