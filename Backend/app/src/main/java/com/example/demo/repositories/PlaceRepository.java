package com.example.demo.repositories;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.example.demo.entities.Place;

public interface PlaceRepository extends JpaRepository<Place, Long> {
  List<Place> findByCategoryId(Long category_id);
  
} 
