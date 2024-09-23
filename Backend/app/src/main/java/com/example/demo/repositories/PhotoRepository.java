package com.example.demo.repositories;

import java.util.Optional;
import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.example.demo.entities.Photo;


public interface PhotoRepository extends JpaRepository<Photo, Long> {
  Optional<Photo> findByFileName(String fileName);

  // @Query("SELECT * FROM category JOIN place ON place.category_id=category.id JOIN photo ON place.id=photo.id WHERE category.category=:category")
  @Query("SELECT p FROM Photo p " +
         "JOIN p.place pl " +
         "JOIN pl.category c " +
         "WHERE c.categoryAsPathVariable = :categoryAsPathVariable")
  List<Photo> findByCategory(@Param("categoryAsPathVariable") String categoryAsPathVariable);

  @Query("SELECT p FROM Photo p " +
         "JOIN p.place pl " +
         "JOIN pl.category c " +
         "WHERE c.categoryAsPathVariable = :categoryAsPathVariable " + 
         "AND pl.placeAsPathVariable = :placeAsPathVariable")
  List<Photo> findByCategoryAndPlace(
    @Param("categoryAsPathVariable") String categoryAsPathVariable,
    @Param("placeAsPathVariable") String placeAsPathVariable);
}
