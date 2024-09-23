package com.example.demo.exceptions;

public class InvalidRequestBodyException extends RuntimeException {

  /**
   * Activates advice with status code of 400 (bad request) with a message.
   * Default message, with param {@code message} = null is "Invalid request body format".
   * @param message a message to be concatenated with default message
   */
  public InvalidRequestBodyException(String message) {
    super("Invalid request body format" + message);
  }
}

