package com.example.demo.exceptions;

public class ServerErrorException extends RuntimeException {

  /**
   * Activates advice with status code of 500 (server error) with a message.
   * Default message, with param {@code message} = null is "Server error occured".
   * @param message a message to be concatenated with default message
   */  
  public ServerErrorException(String message) {
    super("Server error occured" + message);
  }
}
