package com.sparta.notification.server.application.service;

import com.sparta.notification.server.application.service.strategy.CreateNotificationService;
import com.sparta.notification.server.domain.model.vo.NotificationType;
import com.sparta.notification.server.domain.repository.NotificationRepository;
import com.sparta.notification.server.exception.NotificationErrorCode;
import com.sparta.notification.server.exception.NotificationException;
import com.sparta.notification.server.infrastructure.client.UserClient;
import com.sparta.user.user_dto.infrastructure.UserDto;
import dto.NotificationCreateRequest;
import feign.FeignException;
import java.util.List;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Service
@Slf4j(topic = "NotificationFacade")
@RequiredArgsConstructor
public class NotificationFacade {

  private final NotificationRepository notificationRepository;
  private final UserClient userClient;
  private final List<CreateNotificationService> createNotificationServices;

  public Long createNotification(NotificationCreateRequest request) {
    validateUserExists(request.getUserId());

    return createNotificationServices.stream()
        .filter(service -> service.isMatched(
            NotificationType.valueOf(request.getNotificationType())))
        .findAny()
        .orElseThrow()
        .createNotification(request);
  }

  private void validateUserExists(Long userId) {
    try {
      UserDto user = userClient.getUser(userId);
    } catch (FeignException.NotFound e) {
      log.error("User Not Found : {}", userId, e);
      throw new NotificationException(NotificationErrorCode.USER_NOT_FOUND, userId);
    } catch (FeignException e) {
      log.error("User Feign 요청 실패 : ID {}. HTTP status: {}, reason: {}.", userId,
          e.status(), e.getMessage(), e);
      throw new NotificationException(NotificationErrorCode.USER_SERVICE_UNAVAILABLE, userId);
    } catch (Exception e) {
      throw new NotificationException(NotificationErrorCode.INTERNAL_SERVER_ERROR);
    }
  }

}
