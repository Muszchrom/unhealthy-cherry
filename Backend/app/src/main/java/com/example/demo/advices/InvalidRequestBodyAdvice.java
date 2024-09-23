package com.example.demo.advices;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import com.example.demo.exceptions.InvalidRequestBodyException;

@RestControllerAdvice
public class InvalidRequestBodyAdvice {
  
  @ExceptionHandler(InvalidRequestBodyException.class)
  @ResponseStatus(HttpStatus.BAD_REQUEST)
  String invalidRequestBodyHandler(InvalidRequestBodyException ex) {
    return ex.getMessage();
  }  
}
