package com.sparta.notification.server.application.service.strategy;

import com.sparta.notification.server.domain.model.Notification;
import com.sparta.notification.server.domain.model.vo.NotificationType;
import com.sparta.notification.server.domain.repository.NotificationRepository;
import com.sparta.notification.server.exception.NotificationErrorCode;
import com.sparta.notification.server.exception.NotificationException;
import com.sparta.notification.server.infrastructure.client.OrderClient;
import dto.NotificationCreateRequest;
import dto.NotificationOrderDto;
import feign.FeignException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.transaction.annotation.Transactional;


@Slf4j(topic = "CreateOrderNotification")
@RequiredArgsConstructor
public class CreateOrderNotificationStrategy implements CreateNotificationService {

  private final OrderClient orderClient;
  private final NotificationRepository notificationRepository;
  private static final int SINGLE_PRODUCT_QUANTITY = 1;

  @Override
  @Transactional
  public Long createNotification(NotificationCreateRequest request) {
    NotificationOrderDto order = validateOrderExists(request.getTargetId());

    String notificationMessage = createNotificationMessage(order);

    Notification notification = Notification.create(request, notificationMessage);
    return notificationRepository.save(notification).getNotificationId();
  }

  @Override
  public boolean isMatched(NotificationType notificationType) {
    return notificationType.isOrder();
  }

  private String createNotificationMessage(NotificationOrderDto order) {
    if (order.getTotalQuantity() == SINGLE_PRODUCT_QUANTITY) {
      return String.format("%s 상품의 주문이 %s 상태로 변경되었습니다.",
          order.getDisplayProductName(), order.getOrderState());
    } else {
      return String.format("%s 외 %d개 상품의 주문이 %s 상태로 변경되었습니다.",
          order.getDisplayProductName(), order.getTotalQuantity() - 1, order.getOrderState());
    }
  }

  private NotificationOrderDto validateOrderExists(Long orderId) {
    try {
      NotificationOrderDto order = orderClient.getOrder(orderId);
      log.info("Order found: {}", order);
      return order;
    } catch (FeignException.NotFound e) {
      log.error("Order Not Found : {}", orderId, e);
      throw new NotificationException(NotificationErrorCode.ORDER_NOT_FOUND, orderId);
    } catch (FeignException e) {
      log.error("Order Feign 요청 실패 : ID {}. HTTP status: {}, reason: {}.", orderId,
          e.status(), e.getMessage(), e);
      throw new NotificationException(NotificationErrorCode.ORDER_SERVICE_UNAVAILABLE, orderId);
    } catch (Exception e) {
      throw new NotificationException(NotificationErrorCode.INTERNAL_SERVER_ERROR);
    }
  }

}
