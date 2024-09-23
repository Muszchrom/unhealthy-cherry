package com.example.demo.advices;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import com.example.demo.exceptions.ServerErrorException;

@RestControllerAdvice
public class ServerErrorAdvice {
  
  @ExceptionHandler(ServerErrorException.class)
  @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
  String serverErrorHandler(ServerErrorException ex) {
    return ex.getMessage();
  }
}
