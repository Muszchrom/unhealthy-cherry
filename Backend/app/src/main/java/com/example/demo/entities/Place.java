package com.example.demo.entities;

import java.util.List;
import java.util.Objects;

import org.hibernate.annotations.OnDelete;
import org.hibernate.annotations.OnDeleteAction;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;
import jakarta.persistence.UniqueConstraint;

@Entity
@Table(uniqueConstraints={@UniqueConstraint(columnNames={"category_id", "place"})})
public class Place {

  @Column @Id @GeneratedValue(strategy=GenerationType.IDENTITY)
  private Long id;

  @Column(nullable=false)
  private String place;

  @Column(nullable=false)
  private String placeAsPathVariable;

  @ManyToOne
  @JoinColumn(name="category_id", nullable=false)
  @OnDelete(action=OnDeleteAction.CASCADE)
  private Category category;

  @OneToMany(mappedBy="place", cascade=CascadeType.REMOVE, orphanRemoval=true)
  private List<Photo> photos;

  public Place() {}

  public Place(Category category, String place, String placeAsPathVariable) {
    this.place = place;
    this.category = category;
    this.placeAsPathVariable = placeAsPathVariable;
  }

  public Long getId() {
    return this.id;
  }

  public String getPlace() {
    return this.place;
  }

  public String getPlaceAsPathVariable() {
    return this.placeAsPathVariable;
  }

  public Category getCategory() {
    return this.category;
  }

  public void setId(Long id) {
    this.id = id;
  }

  public void setPlace(String place) {
    this.place = place;
  }

  public void setPlaceAsPathVariable(String placeAsPathVariable) {
    this.placeAsPathVariable = placeAsPathVariable;
  }

  public void setCategory(Category category) {
    this.category = category;
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) return true;
    if (!(o instanceof Place)) return false;
    Place placeObj = (Place) o;
    return Objects.equals(this.id, placeObj.id) &&
           Objects.equals(this.place, placeObj.place) &&
           Objects.equals(this.category, placeObj.category) &&
           Objects.equals(this.placeAsPathVariable, placeObj.placeAsPathVariable);
  }

  @Override
  public int hashCode() {
    return Objects.hash(this.id, this.place, this.category, this.placeAsPathVariable);
  }

  @Override
  public String toString() {
    return "Place{" +
             "id=" + this.id + ", " +
             "place=\'" + this.place + "\', " + 
             "placeAsPathVariable=\'" + this.placeAsPathVariable + "\', " + 
             "categoryId=" + this.category + 
           "}";
  }
}
