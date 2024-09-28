package com.entrypoint.gateway;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import com.entrypoint.gateway.entities.User;
import com.entrypoint.gateway.repositories.UserRepository;

/**
 * This configuration class loads the first user, which is admin, into the databse.
 * Password is hashed and log in possible.
 */
@Configuration
public class LoadDatabase {
  private static final Logger log = LoggerFactory.getLogger(LoadDatabase.class);

  @Value("${ADMIN_UNAME}")
  private String adminUsername;

  @Value("${ADMIN_PASSWORD}")
  private String adminPassword;

  @Bean
  CommandLineRunner initDatabase(UserRepository userRepository) {
    return args -> {
      User admin = new User(adminUsername, adminPassword, true);
      admin.hashPassword();
      userRepository.findByUsername(adminUsername)
        .doOnNext(a -> log.info("Admin already exists"))
        .switchIfEmpty(
          userRepository.save(admin)
            .doOnSuccess(savedUser -> log.info("Admin account created"))
        )
        .subscribe();
    };
  }
}
