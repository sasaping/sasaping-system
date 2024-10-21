package com.sparta.promotion.server.infrastructure.configuration;

import org.redisson.Redisson;
import org.redisson.api.RedissonClient;
import org.redisson.config.Config;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class RedissonConfig {

  private static final String REDIS_URL_PREFIX = "redis://";

  @Value("${spring.data.redis.host}")
  private String host;

  @Value("${spring.data.redis.port}")
  private int port;

  @Bean
  RedissonClient redissonClient() {
    Config config = new Config();
    config.useSingleServer().setAddress(REDIS_URL_PREFIX + host + ":" + port);
    return Redisson.create(config);
  }

}
