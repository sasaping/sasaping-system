package com.sparta.product.infrastructure.configuration;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.sparta.product.infrastructure.filter.CustomAccessDeniedHandler;
import com.sparta.product.infrastructure.filter.SecurityContextFilter;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.annotation.web.configurers.RequestCacheConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

@EnableMethodSecurity
@EnableWebSecurity
@RequiredArgsConstructor
@Configuration
@Slf4j(topic = "SecurityConfig")
public class SecurityConfig {
  private final CustomAccessDeniedHandler accessDeniedHandler;

  @Bean
  public SecurityFilterChain httpSecurity(HttpSecurity http, ObjectMapper objectMapper)
      throws Exception {
    http.csrf(AbstractHttpConfigurer::disable)
        .formLogin(AbstractHttpConfigurer::disable)
        .sessionManagement((s) -> s.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
        .rememberMe(AbstractHttpConfigurer::disable)
        .httpBasic(AbstractHttpConfigurer::disable)
        .logout(AbstractHttpConfigurer::disable)
        .requestCache(RequestCacheConfigurer::disable)
        .authorizeHttpRequests(
            authorize ->
                authorize
                    .requestMatchers("/internal/**")
                    .permitAll()
                    .requestMatchers("/api/products/search/**")
                    .permitAll()
                    .requestMatchers("/api/categories/search/**")
                    .permitAll()
                    .requestMatchers("/api/preorders/search/**")
                    .permitAll()
                    .anyRequest()
                    .authenticated())
        .exceptionHandling(
            exceptionHandling ->
                exceptionHandling.accessDeniedHandler(accessDeniedHandler))
        .addFilterAfter(
            new SecurityContextFilter(objectMapper), UsernamePasswordAuthenticationFilter.class);

    return http.build();
  }
}
