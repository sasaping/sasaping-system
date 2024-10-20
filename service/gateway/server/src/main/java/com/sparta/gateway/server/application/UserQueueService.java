package com.sparta.gateway.server.application;

import com.sparta.gateway.server.application.dto.RegisterUserResponse;
import java.time.Instant;
import java.util.Objects;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
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
  private final String USER_ACTIVE_SET_KEY = "users:active";
  private final long MAX_ACTIVE_USERS = 2;

  public Mono<RegisterUserResponse> registerUser(String userId) {
    return reactiveRedisTemplate.opsForSet().size(USER_ACTIVE_SET_KEY)
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
          } else {
            return addToWaitQueue(userId);
          }
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
        .doOnNext(response -> log.info("User {} updated in wait queue at rank {}", userId,
            response.rank()));
  }

  private Mono<RegisterUserResponse> addToProceedQueue(String userId) {
    var unixTime = Instant.now().getEpochSecond();
    return reactiveRedisTemplate.opsForZSet()
        .add(USER_QUEUE_PROCEED_KEY, userId, unixTime)
        .filter(i -> i)
        .switchIfEmpty(Mono.error(new Exception("Failed to add user to proceed queue")))
        .then(reactiveRedisTemplate.opsForSet().add(USER_ACTIVE_SET_KEY, userId))
        .map(i -> new RegisterUserResponse(0L))
        .doOnNext(response -> log.info("User {} is added to proceed queue", userId));
  }

  private Mono<RegisterUserResponse> addToWaitQueue(String userId) {
    var unixTime = Instant.now().getEpochSecond();
    return reactiveRedisTemplate.opsForZSet()
        .add(USER_QUEUE_WAIT_KEY, userId, unixTime)
        .filter(i -> i)
        .switchIfEmpty(Mono.error(new Exception("Failed to add user to wait queue")))
        .flatMap(i -> reactiveRedisTemplate.opsForZSet()
            .rank(USER_QUEUE_WAIT_KEY, userId))
        .map(rank -> new RegisterUserResponse(rank + 1))
        .doOnNext(rank -> log.info("User {} is registered at rank {}", userId, rank.rank()));
  }

  public Mono<Boolean> isAllowed(String userId) {
    return reactiveRedisTemplate.opsForZSet()
        .rank(USER_QUEUE_PROCEED_KEY, userId)
        .defaultIfEmpty(-1L)
        .map(rank -> rank >= 0);
  }

  public Mono<Long> getRank(String userId) {
    return reactiveRedisTemplate.opsForZSet().rank(USER_QUEUE_WAIT_KEY, userId)
        .defaultIfEmpty(-1L)
        .map(rank -> rank >= 0 ? rank + 1 : rank);
  }

  @Scheduled(initialDelay = 50000, fixedRate = 30000)
  public void scheduleAllowUser() {
    log.info("Scheduling allow user task");
    allowUserTask().subscribe(
        movedUsers -> log.info("Moved {} users to proceed queue", movedUsers),
        error -> log.error("Error in scheduled task", error)
    );
  }

  private Mono<Long> allowUserTask() {
    return reactiveRedisTemplate.opsForSet().size(USER_ACTIVE_SET_KEY)
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
          return reactiveRedisTemplate.opsForZSet()
              .add(USER_QUEUE_PROCEED_KEY, userId, Instant.now().getEpochSecond())
              .then(reactiveRedisTemplate.opsForSet().add(USER_ACTIVE_SET_KEY, userId));
        })
        .count();
  }

  // TODO : proceed에 있는 사용자가 이탈했는지 확인하는 방법?
  // 1 - 사용자 활동을 추적해 N 시간동안 활동이 없으면 Proceed 에서 제거
  // 2 - proceed queue의 MAX_ACTIVE_USERS를 일정 시간마다 늘려줌

  // TODO : JwtFilter 전에 Queue filter를 타게 변경..? userId 대신 request Id로 queue 처리


}
