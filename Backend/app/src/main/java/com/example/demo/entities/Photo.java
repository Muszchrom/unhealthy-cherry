package com.example.demo.entities;

import java.sql.Timestamp;
import java.util.Objects;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;

// @Entity is a JPA annotation to make this object ready for storage in a JPA-based data store.
@Entity
@Table(name="photo")
public class Photo {

  @Column(name="id", nullable=false, unique=true) 
  @Id @GeneratedValue(strategy=GenerationType.IDENTITY)
  private Long id;
  
  @Column(name="file_name", nullable=false, unique=true)
  private String fileName;

  @Column(name="file_extension", nullable=false)
  private String fileExtension;

  @ManyToOne
  @JoinColumn(name="place_id", nullable=false)
  private Place place;

  @Column(name="description")
  private String description; 
  
  @Column(name="is_best", nullable=false)
  private Boolean isBest=false; // Every 300 photos should have 10-30 best ones
  
  @Column(name="country")
  private String country;

  @Column(name="camera")
  private String camera;
  
  @Column(name="datetime")
  private Timestamp datetime;


  public Photo() {}

  public Photo(String fileName, String fileExtension, Place place) {
    this.fileName = fileName;
    this.fileExtension = fileExtension;
    this.place = place;
  }

  public Photo(String fileName, String fileExtension, Place place, String description, Boolean isBest, String country, String camera, Timestamp datetime) {
    this.fileName = fileName;
    this.fileExtension = fileExtension;
    this.place = place;
    this.description = description;
    this.isBest = isBest;
    this.country = country;
    this.camera = camera;
    this.datetime = datetime;
  }

  public Long getId() {
    return this.id;
  }

  public String getFileName() {
    return this.fileName;
  }

  public String getFileExtension() {
    return this.fileExtension;
  }

  public Place getPlace() {
    return this.place;
  }

  public String getDescription() {
    return this.description;
  }

  public Boolean getIsBest() {
    return this.isBest;
  }

  public String getCountry() {
    return this.country;
  }

  public String getCamera() {
    return this.camera;
  }

  public Timestamp getDatetime() {
    return this.datetime;
  }

  public void setId(Long id) {
    this.id = id;
  }

  public void setFileName(String fileName) {
    this.fileName = fileName;
  }

  public void setFileExtension(String fileExtension) {
    this.fileExtension = fileExtension;
  }

  public void setPlace(Place place) {
    this.place = place;
  }

  public void setDescription(String description) {
    this.description = description;
  }

  public void setIsBest(Boolean isBest) {
    this.isBest = isBest;
  }

  public void setCountry(String country) {
    this.country = country;
  }

  public void setCamera(String camera) {
    this.camera = camera;
  }

  public void setDatetime(Timestamp datetime) {
    this.datetime = datetime;
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) return true;
    if (!(o instanceof Photo)) return false;
    Photo photo = (Photo) o;
    return Objects.equals(this.id, photo.id) && 
           Objects.equals(this.fileName, photo.fileName) && 
           Objects.equals(this.fileExtension, photo.fileExtension) && 
           Objects.equals(this.place, photo.place) && 
           Objects.equals(this.description, photo.description) && 
           Objects.equals(this.isBest, photo.isBest) && 
           Objects.equals(this.country, photo.country) && 
           Objects.equals(this.camera, photo.camera) && 
           Objects.equals(this.datetime, photo.datetime);
  } 

  @Override
  public int hashCode() {
    return Objects.hash(this.id, this.fileName, this.fileExtension, this.place, this.description, this.isBest, this.country, this.camera, this.datetime);
  }

  @Override
  public String toString() {
    return "Photo{" + 
              "id=" + this.id + ", " + 
              "fileName=\'" + this.fileName + "\', " + 
              "fileExtension=\'" + this.fileExtension + "\', " + 
              "place=\'" + this.place + "\', " +  
              "description=\'" + this.description + "\', " +
              "isBest=" + this.isBest + ", " +  
              "country=\'" + this.country + "\', " +  
              "camera=\'" + this.camera + "\', " +  
              "datetime=" + this.datetime +
            "}";
  }
}