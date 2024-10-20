package com.entrypoint.gateway.entities;

public class UserWithToken {
  private String token;
  private User user;

  public UserWithToken(User u, String token) {
    this.user = u;
    this.token = token;
  }

  public void setToken(String token) {
    this.token = token;
  }

  public String getToken() {
    return this.token;
  }

  public void setUser(User u) {
    this.user = u;
  }

  public User getUser() {
    return this.user;
  }
}
