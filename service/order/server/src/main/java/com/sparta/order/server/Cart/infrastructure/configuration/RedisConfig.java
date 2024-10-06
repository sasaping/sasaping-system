package com.sparta.order.server.Cart.infrastructure.configuration;

import java.util.Map;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.connection.RedisConnectionFactory;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.serializer.GenericJackson2JsonRedisSerializer;
import org.springframework.data.redis.serializer.GenericToStringSerializer;
import org.springframework.data.redis.serializer.StringRedisSerializer;

@Configuration
public class RedisConfig {

  @Bean
  public RedisTemplate<String, Map<String, Integer>> cartRedisTemplate(
      RedisConnectionFactory connectionFactory) {
    RedisTemplate<String, Map<String, Integer>> template = new RedisTemplate<>();
    template.setConnectionFactory(connectionFactory);
    template.setEnableTransactionSupport(true);

    template.setKeySerializer(new StringRedisSerializer());
    template.setValueSerializer(new GenericJackson2JsonRedisSerializer());
    template.setHashKeySerializer(new StringRedisSerializer());
    template.setHashValueSerializer(new GenericToStringSerializer<>(Integer.class));

    return template;
  }

}
