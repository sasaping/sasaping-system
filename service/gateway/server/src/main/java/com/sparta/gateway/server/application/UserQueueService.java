package com.sparta.gateway.server.application;

import com.sparta.gateway.server.application.dto.RegisterUserResponse;
import com.sparta.gateway.server.infrastructure.exception.GatewayErrorCode;
import com.sparta.gateway.server.infrastructure.exception.GatewayException;
import java.time.Instant;
import java.util.Objects;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Range;
import org.springframework.data.redis.core.ReactiveRedisTemplate;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Mono;

@Slf4j
@Service
@RequiredArgsConstructor
public class UserQueueService {

  private static final String USER_QUEUE_WAIT_KEY = "users:queue:wait";
  private static final String USER_QUEUE_ACTIVE_KEY = "users:queue:active";
  private static final long INACTIVITY_THRESHOLD = 300;

  private final ReactiveRedisTemplate<String, String> reactiveRedisTemplate;

  @Value("${MAX_ACTIVE_USERS}")
  private long MAX_ACTIVE_USERS;

  private long getCurrentTime() {
    return Instant.now().getEpochSecond();
  }

  public Mono<RegisterUserResponse> registerUser(String userId) {
    return reactiveRedisTemplate.opsForZSet()
        .add(USER_QUEUE_WAIT_KEY, userId, getCurrentTime())
        .flatMap(success ->
            reactiveRedisTemplate.opsForZSet().rank(USER_QUEUE_WAIT_KEY, userId)
        )
        .map(rank -> new RegisterUserResponse(rank + 1));
  }

  public Mono<Boolean> isAllowed(String userId) {
    return reactiveRedisTemplate.opsForZSet()
        .rank(USER_QUEUE_ACTIVE_KEY, userId)
        .defaultIfEmpty(-1L)
        .map(rank -> rank >= 0)
        .flatMap(isAllowed -> isAllowed ?
            updateUserActivityTime(userId).thenReturn(true) :
            Mono.just(false)
        );
  }

  public Mono<Long> getRank(String userId) {
    return reactiveRedisTemplate.opsForZSet()
        .rank(USER_QUEUE_WAIT_KEY, userId)
        .defaultIfEmpty(-1L)
        .map(rank -> rank >= 0 ? rank + 1 : rank);
  }

  @Scheduled(fixedRate = 10000, initialDelay = 500)
  public void scheduleAllowUser() {
    removeInactiveUsers()
        .then(allowUserTask())
        .subscribe();
  }

  private Mono<Void> removeInactiveUsers() {
    long currentTime = getCurrentTime();
    return reactiveRedisTemplate.opsForZSet()
        .rangeWithScores(USER_QUEUE_ACTIVE_KEY, Range.closed(0L, -1L))
        .filter(userWithScore -> currentTime - userWithScore.getScore() > INACTIVITY_THRESHOLD)
        .flatMap(userWithScore -> removeUser(userWithScore.getValue()))
        .then();
  }

  private Mono<Void> removeUser(String userId) {
    return reactiveRedisTemplate.opsForZSet()
        .remove(USER_QUEUE_ACTIVE_KEY, userId)
        .then();
  }

  private Mono<Long> allowUserTask() {
    return reactiveRedisTemplate.opsForZSet()
        .size(USER_QUEUE_ACTIVE_KEY)
        .flatMap(activeUsers -> {
          long slotsAvailable = MAX_ACTIVE_USERS - activeUsers;
          return slotsAvailable <= 0 ?
              Mono.just(0L) :
              moveUsersToActives(slotsAvailable);
        });
  }

  private Mono<Long> moveUsersToActives(long count) {
    return reactiveRedisTemplate.opsForZSet()
        .popMin(USER_QUEUE_WAIT_KEY, count)
        .flatMap(user -> {
          String userId = Objects.requireNonNull(user.getValue());
          return reactiveRedisTemplate.opsForZSet()
              .add(USER_QUEUE_ACTIVE_KEY, userId, getCurrentTime())
              .filter(Boolean::booleanValue)
              .switchIfEmpty(Mono.error(new GatewayException(GatewayErrorCode.TOO_MANY_REQUESTS)))
              .thenReturn(1L);
        })
        .count();
  }

  private Mono<Boolean> updateUserActivityTime(String userId) {
    return reactiveRedisTemplate.opsForZSet()
        .add(USER_QUEUE_ACTIVE_KEY, userId, getCurrentTime());
  }

}
