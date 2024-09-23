package com.example.demo.advices;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import com.example.demo.exceptions.PhotoNotFoundException;

@RestControllerAdvice
public class PhotoNotFoundAdvice {
  
  @ExceptionHandler(PhotoNotFoundException.class)
  @ResponseStatus(HttpStatus.NOT_FOUND)
  String photoNotFoundHandler(PhotoNotFoundException ex) {
    return ex.getMessage();
  }
}
