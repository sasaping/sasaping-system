package com.sparta.order.server.Cart.infrastructure.configuration;

import com.sparta.order.server.Cart.domain.model.CartProduct;
import com.sparta.order.server.Cart.domain.model.ProductInfo;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.connection.RedisConnectionFactory;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.serializer.Jackson2JsonRedisSerializer;
import org.springframework.data.redis.serializer.StringRedisSerializer;

@Configuration
public class RedisConfig {

  @Bean
  public RedisTemplate<String, CartProduct> userCartRedisTemplate(
      RedisConnectionFactory connectionFactory) {
    RedisTemplate<String, CartProduct> template = new RedisTemplate<>();
    template.setConnectionFactory(connectionFactory);

    template.setKeySerializer(new StringRedisSerializer());
    template.setValueSerializer(new Jackson2JsonRedisSerializer<>(CartProduct.class));
    template.setHashKeySerializer(new StringRedisSerializer());
    template.setHashValueSerializer(new Jackson2JsonRedisSerializer<>(ProductInfo.class));

    return template;
  }

}
