// package com.entrypoint.gateway;

// import org.springframework.context.annotation.Configuration;
// import org.springframework.web.reactive.config.CorsRegistry;
// import org.springframework.web.reactive.config.EnableWebFlux;
// import org.springframework.web.reactive.config.WebFluxConfigurer;

// @Configuration
// @EnableWebFlux
// public class CorsConfig implements WebFluxConfigurer {
  
//   @Override
//   public void addCorsMappings(CorsRegistry corsRegistry) {
//     corsRegistry.addMapping("/**")
//       .allowedOrigins("http://localhost:3000")
//       .allowedMethods("GET", "POST", "PUT", "PATCH", "DELETE")
//       .allowCredentials(true);
//   }
// }
