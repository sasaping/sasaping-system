package com.sparta.product.application.preorder;

import java.util.concurrent.TimeUnit;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.annotation.Aspect;
import org.redisson.api.RLock;
import org.redisson.api.RedissonClient;
import org.springframework.stereotype.Component;

@Aspect
@Component
@RequiredArgsConstructor
@Slf4j(topic = "DistributedLockComponent")
public class DistributedLockComponent {
  private final RedissonClient redissonClient;

  public void execute(
      String lockName, long waitMilliSecond, long leaseMilliSecond, Runnable logic) {
    RLock lock = redissonClient.getLock(lockName);
    try {
      boolean isLocked = lock.tryLock(waitMilliSecond, leaseMilliSecond, TimeUnit.MILLISECONDS);
      if (!isLocked) {
        throw new IllegalStateException("[" + lockName + "] lock 획득 실패");
      }
      logic.run();
    } catch (InterruptedException e) {
      log.error(e.getMessage(), e);
      throw new RuntimeException(e);
    } finally {
      if (lock.isHeldByCurrentThread()) {
        lock.unlock();
      }
    }
  }
}
