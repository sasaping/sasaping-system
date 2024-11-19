package com.sparta.notification.server.presentation.controller;

import com.sparta.common.domain.response.ApiResponse;
import com.sparta.notification.server.application.service.NotificationFacade;
import dto.NotificationCreateRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RequestMapping("/api/notifications")
@RequiredArgsConstructor
@RestController
public class NotificationController {

  private final NotificationFacade notificationFacade;

  @PostMapping("/create")
  public ApiResponse<Long> createOrder(@RequestBody NotificationCreateRequest request) {
    return ApiResponse.created(notificationFacade.createNotification(request));
  }

}
