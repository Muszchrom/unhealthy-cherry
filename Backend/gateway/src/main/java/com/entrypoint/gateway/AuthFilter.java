package com.entrypoint.gateway;

import org.springframework.cloud.gateway.filter.GatewayFilter;
import org.springframework.cloud.gateway.filter.GatewayFilterChain;
import org.springframework.http.HttpStatus;
import org.springframework.http.server.reactive.ServerHttpResponse;
import org.springframework.stereotype.Component;
import org.springframework.http.server.reactive.ServerHttpRequest;
import org.springframework.web.server.ServerWebExchange;

import reactor.core.publisher.Mono;
import io.jsonwebtoken.JwtException;
import io.jsonwebtoken.Jwts;

// TODO: add role filter
@Component
public class AuthFilter implements GatewayFilter {
  
  @Override
  public Mono<Void> filter(ServerWebExchange exchange, GatewayFilterChain chain) {
    ServerHttpRequest request = exchange.getRequest();

    // check if JWT cookie exists
    if (isCookieMissing(request)) {
      return this.onError(
        exchange, 
        "Authorization cookie is missing in request or it's content is invalid", 
        HttpStatus.UNAUTHORIZED);
    }

    String token = request.getCookies().getFirst("JWT").getValue();

    // Check if token is valid
    try {
      Jwts.parser().verifyWith(JWTUtil.getSecretKey()).build().parseSignedClaims(token);
    } catch (JwtException ex) {
      System.out.println(ex.getMessage());
      return this.onError(
        exchange, 
        "Authorization token is invalid", 
        HttpStatus.FORBIDDEN);
    }
    return chain.filter(exchange);
  } 

  private boolean isCookieMissing(ServerHttpRequest request) {
    return null == request.getCookies().getFirst("JWT");
  }

  private Mono<Void> onError(ServerWebExchange exchange, String err, HttpStatus httpStatus) {
    ServerHttpResponse response = exchange.getResponse();
    response.setStatusCode(httpStatus);
    return response.setComplete();
  }
}
