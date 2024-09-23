package com.entrypoint.gateway;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseCookie;
import org.springframework.http.ResponseEntity;
import org.springframework.http.server.reactive.ServerHttpResponse;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import com.entrypoint.gateway.entities.User;
import com.entrypoint.gateway.repositories.UserRepository;

import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

/* TODO:
- ✔️ Pick a db (postgres is fine, cba another one XD) *R2DBC is reactive 
- ✔️ Make the db work
- ✔️ Make username UNIQUE = TRUE
- ✖️ Create post method for log-in. Gateway can point only to other services, excluding its own auth api https://stackoverflow.com/questions/70618318/spring-cloud-gateway-as-a-gateway-as-well-as-web-application
- ✖️ Create post method for register (only admin can register new users) 
- 
- 📌 Working with R2DBC and PSQL https://www.bezkoder.com/spring-boot-r2dbc-postgresql/
- 📌 Basically whole implementation https://www.geeksforgeeks.org/reactive-jwt-authentication-using-spring-webflux/

- ✨ Auto create admin on startup with some docker-compose flags?
*/

@RestController
public class AuthController {
  
  @Value("${TARGET:not_dev}")
  private String appTarget;

  private final UserRepository userRepository;

  AuthController(UserRepository userRepository) {
    this.userRepository = userRepository;
  }

  // DEV
  @PostMapping("/auth/login")
  public Mono<ResponseEntity<String>> login(@RequestBody User user, ServerHttpResponse response) {
    return userRepository.findByUsername(user.getUsername()).map(u -> {
      if (u.getPassword().equals(user.getPassword())) {
        String token = JWTUtil.generateToken(u.getUsername(), u.getIsAdmin());
        ResponseCookie responseCookie = ResponseCookie.from("JWT", token)
          .httpOnly(true)
          .secure(!appTarget.equals("DEV")) // !appTarget.equals("DEV")
          .sameSite("Lax")
          .maxAge(JWTUtil.getJWTExpiration())
          .path("/")
          .build();

        response.addCookie(responseCookie);
        // response.setComplete();
        return ResponseEntity.ok("Logged in");
      } else {
        throw new BadCredentialsException("Invalid username and/or password");
      }
    }).switchIfEmpty(Mono.error(new BadCredentialsException("Invalid username and/or password")));
  }

  @GetMapping("/auth/logout")
  public Mono<Void> logout(ServerHttpResponse response) {
    ResponseCookie responseCookie = ResponseCookie.from("JWT", "")
      .httpOnly(true)
      .secure(!appTarget.equals("DEV"))
      .sameSite("Lax")
      .maxAge(0)
      .path("/")
      .build();

    response.addCookie(responseCookie);
    return response.setComplete();
  }

  @GetMapping("/auth/login")
  public Flux<User> getAllUsers() {
    return userRepository.findAll();
  }
}
