package com.entrypoint.gateway;

import org.springframework.cloud.gateway.filter.GatewayFilter;
import org.springframework.cloud.gateway.filter.GatewayFilterChain;
import org.springframework.http.HttpStatus;
import org.springframework.http.server.reactive.ServerHttpResponse;
import org.springframework.stereotype.Component;
import org.springframework.web.server.ServerWebExchange;

import io.jsonwebtoken.Jwts;
import reactor.core.publisher.Mono;

@Component
public class AuthFilterAdmin implements GatewayFilter{
  /**
   * This method MUST BE used after auth filter is done with normal users.
   * Using this before AuthFilter.filter will result with bunch of 500 errors.
   * Cookie checks are not happening here!
   */
  @Override
  public Mono<Void> filter(ServerWebExchange exchange, GatewayFilterChain chain) {
    String token = exchange.getRequest().getCookies().getFirst("JWT").getValue();
    if ((boolean) Jwts.parser().verifyWith(JWTUtil.getSecretKey()).build().parseSignedClaims(token).getPayload().get("isAdmin")) {
      return chain.filter(exchange);
    } else {
      ServerHttpResponse response = exchange.getResponse();
      response.setStatusCode(HttpStatus.FORBIDDEN);
      return response.setComplete();
    }
  }
}
