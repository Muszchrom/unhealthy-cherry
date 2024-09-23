package com.entrypoint.gateway.entities;

import org.springframework.data.annotation.Id;
import org.springframework.data.relational.core.mapping.Column;
import org.springframework.data.relational.core.mapping.Table;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

@Table("user_db")
public class User {
  @Id
  private Long id;
  private String password;
  private String username;
  @Column("isadmin")
  private Boolean isAdmin;

  public User(String password, String username, Boolean isAdmin) {
    this.password = password;
    this.username = username;
    this.isAdmin = isAdmin;
  }

  public Long getId() {
    return this.id;
  }

  public String getUsername() {
    return this.username;
  }

  // this is actually an autistic take
  // like why would someone manually change password
  public String getPassword() {
    return this.password;
  }

  public Boolean getIsAdmin() {
    return this.isAdmin;
  }

  public void setId(Long id) {
    this.id = id;
  }

  public void setPassword(String password) {
    this.password = new BCryptPasswordEncoder().encode(password);
  }

  public void setUsername(String username) {
    this.username = username;
  }

  public void setIsAdmin(Boolean isAdmin) {
    this.isAdmin = isAdmin;
  }

  @Override
  public String toString() {
    return "User{" +
             "id=" + this.id + ", " +
             "username=\'" + this.username + "\', " +
             "password=\'" + this.password + "\', " +
             "isAdmin=" + this.isAdmin +  
           "}";
  }
}
