package com.sparta.slack.presentation.controller;

import com.sparta.slack.application.dto.MessageRequest;
import com.sparta.slack.application.service.MessageService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@Slf4j
@RequiredArgsConstructor
@RequestMapping("/internal/message")
public class MessageController {

  private final MessageService messageService;

  @PostMapping("")
  public void sendMessage(@RequestBody MessageRequest.Create messageRequest) {
    messageService.sendMessage(messageRequest);
  }

}
