package com.sparta.product.domain.repository.redis;

import lombok.RequiredArgsConstructor;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Repository;

@Repository
@RequiredArgsConstructor
public class RedisRepository {
  private final RedisTemplate<String, String> redisTemplate;

  public Long sAdd(String key, String value) {
    return redisTemplate.opsForSet().add(key, value);
  }

  public Long sCard(String key) {
    return redisTemplate.opsForSet().size(key);
  }

  public Boolean sIsMember(String key, String value) {
    return redisTemplate.opsForSet().isMember(key, value);
  }
}
