package com.entrypoint.gateway;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseCookie;
import org.springframework.http.server.reactive.ServerHttpResponse;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.entrypoint.gateway.entities.User;
import com.entrypoint.gateway.entities.UserWithToken;
import com.entrypoint.gateway.exceptions.InvalidPasswordException;
import com.entrypoint.gateway.exceptions.UserNotFoundException;
import com.entrypoint.gateway.exceptions.UsernameExistsException;
import com.entrypoint.gateway.repositories.UserRepository;

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
@RequestMapping("/auth")
public class AuthController {
  
  @Value("${TARGET:not_dev}")
  private String appTarget;

  private final UserRepository userRepository;

  AuthController(UserRepository userRepository) {
    this.userRepository = userRepository;
  }

  @PostMapping("/login")
  public Mono<UserWithToken> login(@RequestBody User user, ServerHttpResponse response) {
    return userRepository.findByUsername(user.getUsername()).map(u -> {
      BCryptPasswordEncoder bcPsswdEncoder = new BCryptPasswordEncoder();
      if (bcPsswdEncoder.matches(user.getPassword(), u.getPassword())) {
        String token = JWTUtil.generateToken(u.getUsername(), u.getIsAdmin());
        ResponseCookie responseCookie = ResponseCookie.from("JWT", token)
          .httpOnly(true)
          .secure(!appTarget.equals("DEV")) // !appTarget.equals("DEV")
          .sameSite("Lax")
          .maxAge(JWTUtil.getJWTExpiration())
          .path("/")
          .build();

        response.addCookie(responseCookie);
        UserWithToken uwt = new UserWithToken(u, token);
        return uwt;
        // return ResponseEntity.ok("Logged in");
      } else {
        throw new BadCredentialsException("Invalid username and/or password");
      }
    }).switchIfEmpty(Mono.error(new BadCredentialsException("Invalid username and/or password")));
  }

  @GetMapping("/logout")
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
  
  @PostMapping("/user")
  public Mono<User> register(@RequestBody User user) {
    if (user.getPassword().length() < 7) {
      throw new InvalidPasswordException("Password too short");
    }

    return userRepository.findByUsername(user.getUsername())
      .map(u -> {
        Boolean t = true;
        if (t) {
          // why not just this throw line?
          // because of: Type mismatch: cannot convert from Mono<Object> to Mono<User>
          // why not true in if ()? 
          // because Dead code after the if statement
          // Spent too much time trying to make it look better
          // at this point i think r2dbc is just bugged or me and chatbots are lacking some IQ points
          
          throw new UsernameExistsException("Username: " + user.getUsername() + " already exist");
        }
        return u;
      }).switchIfEmpty(Mono.defer(() -> {
        user.hashPassword();
        return userRepository.save(user);
      }));
  }



  @GetMapping("/user/{id}")
  public Mono<User> getUser(@PathVariable Long id) {
    return userRepository.findById(id)
      .switchIfEmpty(Mono.error(new UserNotFoundException("User: " + id + " not found")));
  }
  
  // TODO
  // @PatchMapping("/user/{id}")
  // public Mono<User> patchUser(@RequestBody User user) {
  //   return userRepository
  // }

  @DeleteMapping("/user/{id}")
  public Mono<Void> deleteUser(@PathVariable Long id) {
    return userRepository.deleteById(id);
  }
}
