package com.sparta.gateway.server.infrastructure.configuration;

import feign.Logger;
import feign.codec.Decoder;
import org.springframework.beans.factory.ObjectFactory;
import org.springframework.boot.autoconfigure.http.HttpMessageConverters;
import org.springframework.cloud.openfeign.support.SpringDecoder;
import org.springframework.context.annotation.Bean;

public class AuthFeignConfig {

  @Bean
  public Decoder feignDecoder() {
    ObjectFactory<HttpMessageConverters> messageConverters = HttpMessageConverters::new;
    return new SpringDecoder(messageConverters);
  }

  @Bean
  public Logger.Level feignLoggerLevel() {
    return Logger.Level.FULL;
  }
}
