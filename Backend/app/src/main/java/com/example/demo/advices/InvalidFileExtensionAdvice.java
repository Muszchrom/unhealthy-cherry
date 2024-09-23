package com.example.demo.advices;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import com.example.demo.exceptions.InvalidFileExtensionException;

@RestControllerAdvice
public class InvalidFileExtensionAdvice {
  @ExceptionHandler(InvalidFileExtensionException.class)
  @ResponseStatus(HttpStatus.BAD_REQUEST) // dunno might be invalid
  String InvalidFileExtensionHandler(InvalidFileExtensionException ex) {
    return ex.getMessage() + "\n" + "Unsupported file MIME type. Valid image conent types: jpg, png, webp, bmp, avif, gif, tiff.";
  }
}