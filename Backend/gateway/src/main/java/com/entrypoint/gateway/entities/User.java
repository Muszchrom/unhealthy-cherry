package com.entrypoint.gateway.entities;

import org.springframework.data.annotation.Id;
import org.springframework.data.relational.core.mapping.Column;
import org.springframework.data.relational.core.mapping.Table;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

/**
 * This entitie's constructor doesn't hash password by default.
 * To hash the password, you need to manually run 
 * {@link #setPassword setPassword(String)} or 
 * {@link #hashPassword hashPassword()} method.
 * This behavior lets you validate the password by for example, checking it's length. 
 * <pre class="code">
 * public Mono&lt;User&gt; register(@RequestBody User user) {<br>
 *   if (user.getPassword().length() < 8) throw new RuntimeException();
 *   user.setPassword(user.getPassword()); // hashing the password
 *   return userRepository.save(user);
 * }
 * <pre class="code">
 */
@Table("user_db")
public class User {
  @Column("id") @Id
  private Long id;
  @Column("password")
  private String password;
  @Column("username")
  private String username;
  @Column("is_admin")
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

  // this is actually an bad take
  // like why would someone manually change password
  // To read it's length? XD
  /**
   * You can't get plain password after hashing it
   * @return Either plain or hashed password
   */
  public String getPassword() {
    return this.password;
  }

  public Boolean getIsAdmin() {
    return this.isAdmin;
  }

  public void setId(Long id) {
    this.id = id;
  }

  /**
   * Set and hash the password at the same time.
   * @param password a password to be set and hashed.
   */
  public void setPassword(String password) {
    this.password = new BCryptPasswordEncoder().encode(password);
  }

  public void setUsername(String username) {
    this.username = username;
  }

  public void setIsAdmin(Boolean isAdmin) {
    this.isAdmin = isAdmin;
  }

  /**
   * Hash the current password.
   */
  public void hashPassword() {
    this.password = new BCryptPasswordEncoder().encode(this.password);
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
