package com.example.demo.exceptions;

public class InvalidFileExtensionException extends RuntimeException{
  public InvalidFileExtensionException(String fileHash) {
    super("Invalid file extension/format: " + fileHash);
  }
}
