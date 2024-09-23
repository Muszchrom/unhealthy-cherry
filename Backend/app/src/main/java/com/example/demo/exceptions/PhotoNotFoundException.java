package com.example.demo.exceptions;

public class PhotoNotFoundException extends RuntimeException {
  public PhotoNotFoundException(Long id) {
    super("Could not find photo " + id);
  }

  public PhotoNotFoundException(String fileName) {
    super("Could not find photo " + fileName);
  }
}
