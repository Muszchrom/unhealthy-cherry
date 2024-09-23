package com.example.demo.advices;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import com.example.demo.exceptions.PlaceNotFoundException;

@RestControllerAdvice
public class PlaceNotFoundAdvice {

  @ExceptionHandler(PlaceNotFoundException.class)
  @ResponseStatus(HttpStatus.NOT_FOUND)
  String PlaceNotFoundHandler(PlaceNotFoundException ex) {
    return ex.getMessage();
  }
}
