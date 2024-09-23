package com.example.demo.advices;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import com.example.demo.exceptions.PhotoExistsException;

@RestControllerAdvice
public class PhotoExistsAdvice {

  @ExceptionHandler(PhotoExistsException.class)
  @ResponseStatus(HttpStatus.NOT_MODIFIED)
  String photoExistsHandler(PhotoExistsException ex) {
    return ex.getMessage();
  }
}
