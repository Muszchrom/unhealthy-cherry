package com.example.demo;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import org.springframework.core.io.Resource;

import com.example.demo.FileProcessingService.ResourceWithContentType;
import com.example.demo.entities.Category;
import com.example.demo.entities.Photo;
import com.example.demo.entities.Place;
import com.example.demo.exceptions.InvalidRequestBodyException;
import com.example.demo.exceptions.PhotoExistsException;
import com.example.demo.exceptions.PhotoNotFoundException;
import com.example.demo.exceptions.PlaceNotFoundException;
import com.example.demo.exceptions.ServerErrorException;
import com.example.demo.repositories.CategoryRepository;
import com.example.demo.repositories.PhotoRepository;
import com.example.demo.repositories.PlaceRepository;
import com.fasterxml.jackson.databind.ObjectMapper;


import java.sql.Timestamp;

@RestController
public class PhotoController {

  @Autowired
  private FileProcessingService fileProcessingService;

  // private final XXX XXX;
  private final CategoryRepository categoryRepository;
  private final PlaceRepository placeRepository;
  private final PhotoRepository photoRepository;

  PhotoController(CategoryRepository categoryRepository, PlaceRepository placeRepository, PhotoRepository photoRepository) {
    this.photoRepository = photoRepository;
    this.categoryRepository = categoryRepository;
    this.placeRepository = placeRepository;
  }

  @GetMapping("/categories")
  List<Category> allCategories() {
    return categoryRepository.findAll();
  }

  @PostMapping("/categories")
  Category newCategory(@RequestBody Category newCategory) {
    return categoryRepository.save(newCategory);
  }

  @PutMapping("/categories/{id}")
  Category replaceCategory(@RequestBody Category newCategory, @PathVariable Long id) {
    return categoryRepository.findById(id).map(category -> {
      category.setCategory(newCategory.getCategory());
      return categoryRepository.save(category);
    }).orElseGet(() -> {
      return categoryRepository.save(newCategory);
    });
  }

  @DeleteMapping("/categories/{id}")
  void deleteCategory(@PathVariable Long id) {
    categoryRepository.deleteById(id);
  }


  @GetMapping("/places")
  List<Place> allPlaces() {
    return placeRepository.findAll();
  }

  @PostMapping("/places")
  Place newPlace(@RequestBody Place newPlace) {
    return placeRepository.save(newPlace);
  }

  @PutMapping("/places/{id}")
  Place replacePlace(@RequestBody Place newPlace, @PathVariable Long id) {
    return placeRepository.findById(id).map(place -> {
      place.setCategory(newPlace.getCategory());
      place.setPlace(newPlace.getPlace());
      return placeRepository.save(place);
    }).orElseGet(() -> {
      return placeRepository.save(newPlace);
    });
  }

  @DeleteMapping("/places/{id}")
  void deletePlace(@PathVariable Long id) {
    placeRepository.deleteById(id);
  }


  @GetMapping("/photo/{fileName}")
  ResponseEntity<Resource> serveFile(@PathVariable String fileName) {
    if (!fileName.matches("^[a-f0-9]{64}.[a-z]{3,4}$")) {
      throw new PhotoNotFoundException(fileName);
    }

    ResourceWithContentType file = fileProcessingService.downloadFile(fileName);

    if (file == null) {
      throw new PhotoNotFoundException(fileName);
    }

    return ResponseEntity.ok()
            .contentType(file.getMediaType())
            .header(HttpHeaders.CONTENT_DISPOSITION, "attachmentl filename=\"" + file.getResource().getFilename() + "\"")
            .body(file.getResource());
  }

  @GetMapping("/photos")
  List<Photo> allPhotos() {
    return photoRepository.findAll();
  }

  @GetMapping("/photos/{category}")
  List<Photo> allPhotosInCategory(@PathVariable String category) {
    return photoRepository.findByCategory(category);
  } 

  @GetMapping("/photos/{category}/{place}")
  List<Photo> allPhotosInCategoryAndPlace(@PathVariable String category, @PathVariable String place) {
    return photoRepository.findByCategoryAndPlace(category, place);
  }

  @PostMapping("/photos")
  Photo uploadFile(
    @RequestParam(name="file") MultipartFile file, 
    @RequestParam(name="details") String details
  ) {
  
    // possible tweaks in app.props
    // spring.servlet.multipart.max-file-size=20MB
    // spring.servlet.multipart.max-request-size=20MB
    // TODO:
    // Max file size exceded error handler
    // very good guide https://spring.io/guides/gs/uploading-files

    if (file == null || details == null) {
      throw new InvalidRequestBodyException(". Multipart form data must include a \"file\" and a \"deatails\" fields.");
    } 

    fileProcessingService.setMultipartFile(file);

    Photo newPhoto = handlePhotoStringified(details, "POST", null);
    newPhoto.setFileName(fileProcessingService.getFileHash());
    newPhoto.setFileExtension(fileProcessingService.getFileExtension());

    String status = fileProcessingService.uploadFile();
    if (status.equals("EXISTS")) {
      throw new PhotoExistsException(" on the server.");
    }
    if (!(status.equals("CREATED"))) {
      throw new ServerErrorException(" while processing the file.");
    }

    return photoRepository.save(newPhoto);
  } 

  @PatchMapping("/photos/{id}")
  Photo replacePhoto(
    @PathVariable Long id, 
    @RequestParam(name="details", required=false) String details, 
    @RequestParam(name="file", required=false) MultipartFile file
  ) {

    Photo photo;

    if (file == null && details != null) { // only details provided
      photo = handlePhotoStringified(details, "PATCH", id);
    } else if (file != null && details == null) { // only file provided
      photo = photoRepository.findById(id).orElseThrow(() -> new PhotoNotFoundException(id));
    } else if (file != null && details != null) { // both provided
      photo = handlePhotoStringified(details, "PATCH", id); 
    } else { // none provided
      throw new InvalidRequestBodyException(". Multipart form data must include a \"file\" or \"deatails\" field or both.");
    }

    if (file != null) {
      fileProcessingService.setMultipartFile(file);
      String status = fileProcessingService.uploadFile(photo.getFileName() + "." + photo.getFileExtension());
      if (status.equals("FAILED")) {
        throw new ServerErrorException(" while processing the file.");
      } 
      if (status.equals("EXISTS")) {
        throw new PhotoExistsException(" on the server.");
      }
      photo.setFileName(fileProcessingService.getFileHash());
      photo.setFileExtension(fileProcessingService.getFileExtension());
    }

    return photoRepository.save(photo);
  }

  @DeleteMapping("/photos/{id}")
  void deletePhoto(@PathVariable Long id) {
    Photo photo = photoRepository.findById(id).orElseThrow(() -> new PhotoNotFoundException(id));
    fileProcessingService.deleteFile(photo.getFileName() + "." + photo.getFileExtension());
    photoRepository.deleteById(photo.getId());
  }

  /**
   * Used for parsing stringified JSON into Photo. Depending on method, it either returns altered, existing Photo or creates new one. 
   * This method also validates the data provided, and throws exceptions if critical conditions are not met.
   * @param photoStringified JSON.stringify() version of Photo
   * @param method ether {@code POST} or {@code PATCH}
   * @param id must not be null in {@code PATCH} 
   * @return new {@code Photo} in case of {@code POST} and altered {@code Photo} in case of {@code PATCH}
   */
  private Photo handlePhotoStringified(String photoStringified, String method, Long id) {
    Photo oldPhoto; // well actually a newPhoto
    Photo photo; // Photo object from photoStringified

    // Transform photo stringified to Photo
    try {
      photo = new ObjectMapper().readValue(photoStringified, Photo.class);
    } catch (Exception ex) {
      throw new InvalidRequestBodyException(". Could not parse \"photo\" field");
    }

    if (method == "PATCH") {
      oldPhoto = photoRepository.findById(id).orElseThrow(() -> new PhotoNotFoundException(id));
    } else {
      oldPhoto = new Photo();
    }

    if (method == "POST" && (photo.getPlace() == null || photo.getPlace().getId() == null)) {
      throw new InvalidRequestBodyException(". Place id is not defined");
    }
    if (photo.getPlace() != null && photo.getPlace().getId() != null) {
      Place place = placeRepository.findById(photo.getPlace().getId()).orElseThrow(() -> new PlaceNotFoundException(photo.getPlace().getId()));
      oldPhoto.setPlace(place);
    } 

    if (photo.getIsBest() != null) {
      oldPhoto.setIsBest(photo.getIsBest());
    } else {
      oldPhoto.setIsBest(false);
    }

    Timestamp t = photo.getDatetime();
    if (t != null && t.getNanos() == 0) {
      oldPhoto.setDatetime(null);
    } else if (t != null) {
      oldPhoto.setDatetime(t);
    }

    if (photo.getCamera() != null) {
      oldPhoto.setCamera(photo.getCamera());
    }

    if (photo.getCountry() != null) {
      oldPhoto.setCountry(photo.getCountry());
    }

    if (photo.getDescription() != null) {
      oldPhoto.setDescription(photo.getDescription());
    }

    return oldPhoto;
  }
}
