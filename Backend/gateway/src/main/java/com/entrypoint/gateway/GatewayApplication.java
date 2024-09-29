package com.entrypoint.gateway;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.security.reactive.ReactiveSecurityAutoConfiguration;
import org.springframework.cloud.gateway.route.RouteLocator;
import org.springframework.cloud.gateway.route.builder.RouteLocatorBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.data.r2dbc.repository.config.EnableR2dbcRepositories;
import org.springframework.http.HttpMethod;


@EnableR2dbcRepositories
@SpringBootApplication(exclude={ReactiveSecurityAutoConfiguration.class})
public class GatewayApplication {

  @Autowired
  private AuthFilter authFilter;

  @Autowired
  private AuthFilterAdmin authFilterAdmin;

	public static void main(String[] args) {
		SpringApplication.run(GatewayApplication.class, args);
	}

  @Bean
  public RouteLocator myRoutes(RouteLocatorBuilder builder) {
    return builder.routes()
      .route("😎 Normal users for Photos app", p -> p
        .path("/photos/**")
        .and()
        .method(HttpMethod.GET)
        .filters(f -> {
          f.rewritePath("/photos/(?<segment>.*)", "/${segment}");
          f.filter(authFilter);
          return f;
        })
        .uri("http://backend:8080"))
      
      .route("🛡️ Admin for Photos app", p -> p
        .path("/photos/**")
        .and()
        .method(HttpMethod.POST, HttpMethod.PUT, HttpMethod.PATCH, HttpMethod.DELETE)
        .filters(f -> {
          f.rewritePath("/photos/(?<segment>.*)", "/${segment}");
          f.filter(authFilter);
          f.filter(authFilterAdmin);
          return f;
        })
        .uri("http://backend:8080"))

        .route("😎 Auth for normal users", p -> p
          .path("/auth/login", "/auth/logout")
          .and()
          .method(HttpMethod.GET, HttpMethod.POST)
          .uri("http://localhost:8081")
        )

        .route("🛡️ Auth for admin", p -> p
          .path("/auth/**")
          .filters(f -> {
            f.filter(authFilter);
            f.filter(authFilterAdmin);
            return f;
          })
          .uri("http://localhost:8081")
        )
      .build();
  }
}

