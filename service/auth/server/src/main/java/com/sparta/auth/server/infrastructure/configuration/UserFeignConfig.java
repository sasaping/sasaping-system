package com.sparta.auth.server.infrastructure.configuration;

import com.sparta.auth.server.infrastructure.feign.UserErrorDecoder;
import feign.Logger;
import feign.codec.ErrorDecoder;
import org.springframework.context.annotation.Bean;

public class UserFeignConfig {

  @Bean
  public ErrorDecoder errorDecoder() {
    return new UserErrorDecoder();
  }

  @Bean
  public Logger.Level feignLoggerLevel() {
    return Logger.Level.FULL;
  }

}
