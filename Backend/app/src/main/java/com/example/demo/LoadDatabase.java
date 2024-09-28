package com.example.demo;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.dao.DataIntegrityViolationException;

import com.example.demo.entities.Category;
import com.example.demo.entities.Photo;
import com.example.demo.entities.Place;
import com.example.demo.repositories.CategoryRepository;
import com.example.demo.repositories.PhotoRepository;
import com.example.demo.repositories.PlaceRepository;

@Configuration
public class LoadDatabase {
  private static final Logger log = LoggerFactory.getLogger(LoadDatabase.class);

  
  // all CommandLineRunner beans are ran after app context is loaded
  @Bean
  CommandLineRunner initDatabase(CategoryRepository categoryRepository, PlaceRepository placeRepository, PhotoRepository photoRepository) {
    return args -> {
      try {
        Category category1 = new Category("Grecja", "grecja");
        Category category2 = new Category("Góry stołowe", "gory-stolowe");
        log.info("Preloading " + categoryRepository.save(category1));
        log.info("Preloading " + categoryRepository.save(category2));
  
        Place place1 = new Place(category1, "Santorini", "santorini");
        Place place2 = new Place(category1, "Ateny", "ateny");
        Place place3 = new Place(category2, "Mglisty szlak", "mglisty-szlak");
        log.info("Preloading " + placeRepository.save(place1));
        log.info("Preloading " + placeRepository.save(place2));
        log.info("Preloading " + placeRepository.save(place3));
        
        Photo photo1 = new Photo("hashedfile1", "jpg", place1);
        Photo photo2 = new Photo("hashedfile2", "jpg", place2);
        Photo photo3 = new Photo("hashedfile3", "jpg", place3);
        Photo photo4 = new Photo("hashedfile4", "jpg", place3);
        log.info("Preloading " + photoRepository.save(photo1));
        log.info("Preloading " + photoRepository.save(photo2));
        log.info("Preloading " + photoRepository.save(photo3));
        log.info("Preloading " + photoRepository.save(photo4));
      } catch (DataIntegrityViolationException ex) { // lazy ahh solution
        log.info("Data already preloaded");
      }
    };
  }
}
