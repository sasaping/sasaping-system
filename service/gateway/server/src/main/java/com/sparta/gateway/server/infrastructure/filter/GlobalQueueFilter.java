package com.sparta.gateway.server.infrastructure.filter;

import static com.sparta.common.domain.jwt.JwtGlobalConstant.X_USER_CLAIMS;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.sparta.auth.auth_dto.jwt.JwtClaim;
import com.sparta.gateway.server.application.UserQueueService;
import java.net.URLDecoder;
import java.nio.charset.StandardCharsets;
import lombok.extern.slf4j.Slf4j;
import org.springframework.cloud.gateway.filter.GatewayFilterChain;
import org.springframework.cloud.gateway.filter.GlobalFilter;
import org.springframework.core.Ordered;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;
import org.springframework.web.server.ServerWebExchange;
import reactor.core.publisher.Mono;

@Component
@Slf4j
public class GlobalQueueFilter implements GlobalFilter, Ordered {

  private final UserQueueService userQueueService;
  private final ObjectMapper objectMapper;

  public GlobalQueueFilter(UserQueueService userQueueService, ObjectMapper objectMapper) {
    this.userQueueService = userQueueService;
    this.objectMapper = objectMapper;
  }

  @Override
  public Mono<Void> filter(ServerWebExchange exchange, GatewayFilterChain chain) {
    String path = exchange.getRequest().getURI().getPath();

    if (path.startsWith("/api/auth/")
        || path.startsWith("/api/users/sign-up")
        || path.startsWith("/api/search")
        || path.startsWith("/api/products/search")
        || path.startsWith("/api/preorder/search")
        || path.startsWith("/api/categories/search")) {
      return chain.filter(exchange);
    }
    
    String encodedClaims = exchange.getRequest().getHeaders().getFirst(X_USER_CLAIMS);
    if (encodedClaims == null) {
      exchange.getResponse().setStatusCode(HttpStatus.UNAUTHORIZED);
      return exchange.getResponse().setComplete();
    }
    String decodedClaims = URLDecoder.decode(encodedClaims, StandardCharsets.UTF_8);
    JwtClaim claims;
    try {
      claims = objectMapper.readValue(decodedClaims, JwtClaim.class);
    } catch (JsonProcessingException e) {
      log.error("Error parsing JWT claims", e);
      exchange.getResponse().setStatusCode(HttpStatus.BAD_REQUEST);
      return exchange.getResponse().setComplete();
    }
    String userId = claims.getUserId().toString();

    return userQueueService.isAllowed(userId)
        .flatMap(isAllowed -> {
          if (isAllowed) {
            return chain.filter(exchange);
          } else {
            return userQueueService.registerUser(userId)
                .flatMap(response -> {
                  if (response.rank() == 0) {
                    return chain.filter(exchange);
                  } else {
                    exchange.getResponse().setStatusCode(HttpStatus.TOO_MANY_REQUESTS);
                    exchange.getResponse().getHeaders()
                        .add("X-Queue-Rank", String.valueOf(response.rank()));
                    return exchange.getResponse().setComplete()
                        .then(Mono.fromRunnable(() ->
                            log.info("User {} is in wait queue at rank {}", userId,
                                response.rank())));
                  }
                });
          }
        })
        .onErrorResume(e -> {
          log.error("Error in GlobalQueueFilter", e);
          exchange.getResponse().setStatusCode(HttpStatus.INTERNAL_SERVER_ERROR);
          return exchange.getResponse().setComplete();
        });
  }

  @Override
  public int getOrder() {
    // Set the order of this filter
    // You might want to adjust this value depending on when you want this filter to run
    return Ordered.LOWEST_PRECEDENCE;
  }

}
