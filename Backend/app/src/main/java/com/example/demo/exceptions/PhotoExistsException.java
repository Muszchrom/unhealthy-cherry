package com.example.demo.exceptions;

public class PhotoExistsException extends RuntimeException {
  public PhotoExistsException(String hashName) {
    super("Photo already exists" + hashName);
  }
}
