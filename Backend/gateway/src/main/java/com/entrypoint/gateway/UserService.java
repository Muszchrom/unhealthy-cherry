package com.entrypoint.gateway;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.entrypoint.gateway.entities.User;
import com.entrypoint.gateway.repositories.UserRepository;

import reactor.core.publisher.Mono;

@Service
public class UserService {
  @Autowired
  UserRepository userRepository;
  
  public Mono<User> save(User user) {
    return userRepository.save(user);
  }
}
