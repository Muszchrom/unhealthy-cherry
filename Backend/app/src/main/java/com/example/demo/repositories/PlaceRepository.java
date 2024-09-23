package com.example.demo.repositories;

import org.springframework.data.jpa.repository.JpaRepository;

import com.example.demo.entities.Place;

public interface PlaceRepository extends JpaRepository<Place, Long> {

  
} 
