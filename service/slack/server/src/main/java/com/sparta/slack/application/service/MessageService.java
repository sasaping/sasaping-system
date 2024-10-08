package com.sparta.slack.application.service;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.sparta.slack.application.dto.MessageRequest;
import com.sparta.slack.domain.entity.Message;
import com.sparta.slack.domain.repository.MessageRepository;
import com.sparta.slack.exception.MessageErrorCode;
import com.sparta.slack.exception.MessageException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

@Service
@Slf4j
@RequiredArgsConstructor
public class MessageService {

  private final MessageRepository messageRepository;
  private final RestTemplate restTemplate = new RestTemplate();

  @Value("${SLACK_TOKEN}")
  private String slackToken;

  private static final String SLACK_API_URL = "https://slack.com/api/chat.postMessage";

  public void sendMessage(MessageRequest.Create messageRequest) {

    String slackUserId = getSlackUserId(messageRequest.getReceiverEmail());

    messageRepository.save(Message.create(messageRequest));

    HttpHeaders headers = new HttpHeaders();
    headers.setContentType(MediaType.APPLICATION_JSON);
    headers.setBearerAuth(slackToken);

    String requestBody =
        String.format(
            "{\"channel\":\"%s\", \"text\":\"%s\"}", slackUserId, messageRequest.getMessage());

    HttpEntity<String> entity = new HttpEntity<>(requestBody, headers);
    restTemplate.postForEntity(SLACK_API_URL, entity, String.class);
  }

  public String getSlackUserId(String email) {

    String url = "https://slack.com/api/users.lookupByEmail";

    HttpHeaders headers = new HttpHeaders();
    headers.setContentType(MediaType.APPLICATION_JSON);
    headers.setBearerAuth(slackToken);

    HttpEntity<String> request = new HttpEntity<>(headers);

    try {
      String apiUrlWithParams = url + "?email=" + email;
      ResponseEntity<String> response =
          restTemplate.exchange(apiUrlWithParams, HttpMethod.GET, request, String.class);

      if (response.getStatusCode() == HttpStatus.OK) {
        ObjectMapper objectMapper = new ObjectMapper();
        JsonNode root = objectMapper.readTree(response.getBody());
        if (root.path("ok").asBoolean()) {
          return root.path("user").path("id").asText();
        }
      }
    } catch (Exception e) {
      log.error(e.getMessage());
      throw new MessageException(MessageErrorCode.INVALID_PARAMETER);
    }
    throw new MessageException(MessageErrorCode.USER_NOT_FOUND);
  }
}
