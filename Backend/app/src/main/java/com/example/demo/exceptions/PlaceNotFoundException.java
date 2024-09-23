package com.example.demo.exceptions;

public class PlaceNotFoundException extends RuntimeException {
  public PlaceNotFoundException(Long placeId) {
    super("Could not find place " + placeId);
  }
}
