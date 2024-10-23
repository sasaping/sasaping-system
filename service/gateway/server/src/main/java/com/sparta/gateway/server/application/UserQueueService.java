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

  private final ReactiveRedisTemplate<String, String> reactiveRedisTemplate;
  private final String USER_QUEUE_WAIT_KEY = "users:queue:wait";
  private final String USER_QUEUE_PROCEED_KEY = "users:queue:proceed";
  @Value("${MAX_ACTIVE_USERS}")
  private long MAX_ACTIVE_USERS;

  public Mono<RegisterUserResponse> registerUser(String userId) {
    reactiveRedisTemplate.opsForZSet().size(USER_QUEUE_PROCEED_KEY).block();
    return reactiveRedisTemplate.opsForZSet().size(USER_QUEUE_PROCEED_KEY)
        .flatMap(activeUsers -> {
          if (activeUsers < MAX_ACTIVE_USERS) {
            return addToProceedQueue(userId);
          } else {
            return checkAndAddToQueue(userId);
          }
        });
  }

  private Mono<RegisterUserResponse> checkAndAddToQueue(String userId) {
    return reactiveRedisTemplate.opsForZSet().score(USER_QUEUE_WAIT_KEY, userId)
        .defaultIfEmpty(-1.0)
        .flatMap(score -> {
          if (score >= 0) {
            return updateWaitQueueScore(userId);
          }
          return addToWaitQueue(userId);

        });
  }

  private Mono<RegisterUserResponse> updateWaitQueueScore(String userId) {
    double newScore = Instant.now().getEpochSecond();
    return reactiveRedisTemplate.opsForZSet().score(USER_QUEUE_WAIT_KEY, userId)
        .flatMap(oldScore ->
            reactiveRedisTemplate.opsForZSet().add(USER_QUEUE_WAIT_KEY, userId, newScore)
                .then(reactiveRedisTemplate.opsForZSet().rank(USER_QUEUE_WAIT_KEY, userId))
        )
        .map(rank -> new RegisterUserResponse(rank + 1))
        ;
  }

  private Mono<RegisterUserResponse> addToProceedQueue(String userId) {
    var unixTime = Instant.now().getEpochSecond();
    return reactiveRedisTemplate.opsForZSet()
        .add(USER_QUEUE_PROCEED_KEY, userId, unixTime)
        .filter(i -> i)
        .flatMap(
            success -> updateUserActivityTime(userId).thenReturn(new RegisterUserResponse(0L)));
  }

  private Mono<RegisterUserResponse> addToWaitQueue(String userId) {
    var unixTime = Instant.now().getEpochSecond();
    return reactiveRedisTemplate.opsForZSet()
        .add(USER_QUEUE_WAIT_KEY, userId, unixTime)
        .filter(i -> i)
        .switchIfEmpty(Mono.error(new GatewayException(GatewayErrorCode.TOO_MANY_REQUESTS)))
        .flatMap(i -> reactiveRedisTemplate.opsForZSet()
            .rank(USER_QUEUE_WAIT_KEY, userId))
        .map(rank -> new RegisterUserResponse(rank + 1))
        ;
  }

  public Mono<Boolean> isAllowed(String userId) {
    return reactiveRedisTemplate.opsForZSet()
        .rank(USER_QUEUE_PROCEED_KEY, userId)
        .defaultIfEmpty(-1L)
        .map(rank -> rank >= 0)
        .flatMap(isAllowed -> {
          if (isAllowed) {
            return updateUserActivityTime(userId).thenReturn(true);
          }
          return registerUser(userId).thenReturn(true);
        });
  }

  public Mono<Long> getRank(String userId) {
    return reactiveRedisTemplate.opsForZSet().rank(USER_QUEUE_WAIT_KEY, userId)
        .defaultIfEmpty(-1L)
        .map(rank -> rank >= 0 ? rank + 1 : rank);
  }

  @Scheduled(fixedRate = 30000)
  public void scheduleAllowUser() {
    removeInactiveUsers()
        .then(allowUserTask())
        .subscribe(
            movedUsers -> {
            },
            error -> {
              log.error(GatewayErrorCode.INTERNAL_SERVER_ERROR.getMessage(), error);
            }
        );
  }

  private Mono<Void> removeInactiveUsers() {
    long currentTime = Instant.now().getEpochSecond();
    double maxScore = currentTime - 300;

    return reactiveRedisTemplate.opsForZSet()
        .removeRangeByScore(USER_QUEUE_PROCEED_KEY, Range.closed(0.0, maxScore))
        .then();
  }

  private Mono<Long> allowUserTask() {
    return reactiveRedisTemplate.opsForZSet().size(USER_QUEUE_PROCEED_KEY)
        .flatMap(activeUsers -> {
          long slotsAvailable = MAX_ACTIVE_USERS - activeUsers;
          if (slotsAvailable <= 0) {
            return Mono.just(0L);
          }
          return moveUsersToProceeds(slotsAvailable);
        });
  }

  private Mono<Long> moveUsersToProceeds(long count) {
    return reactiveRedisTemplate.opsForZSet()
        .popMin(USER_QUEUE_WAIT_KEY, count)
        .flatMap(user -> {
          String userId = Objects.requireNonNull(user.getValue());
          return updateUserActivityTime(userId);
        })
        .count();
  }

  private Mono<Boolean> updateUserActivityTime(String userId) {
    long currentTime = Instant.now().getEpochSecond();
    return reactiveRedisTemplate.opsForZSet().add(USER_QUEUE_PROCEED_KEY, userId, currentTime);
  }

}
