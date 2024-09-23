package com.example.demo.entities;

import java.util.List;
import java.util.Objects;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.OneToMany;

@Entity
public class Category {

  @Column @Id @GeneratedValue(strategy=GenerationType.IDENTITY)
  private Long id;

  @Column(unique=true, nullable=false)
  private String category;

  @Column(unique=true, nullable=false)
  private String categoryAsPathVariable;

  @OneToMany(mappedBy="category", cascade=CascadeType.REMOVE, orphanRemoval=true)
  private List<Place> places;


  public Category() {}

  public Category(String category, String categoryAsPathVariable) {
    this.category = category;
    this.categoryAsPathVariable = categoryAsPathVariable;
  }

  public Long getId() {
    return this.id;
  }

  public String getCategory() {
    return this.category;
  }

  public String getCategoryAsPathVariable() {
    return this.categoryAsPathVariable;
  }

  public void setId(Long id) {
    this.id = id;
  }

  public void setCategory(String category) {
    this.category = category;
  }

  public void setCategoryAsPathVariable(String categoryAsPathVariable) {
    this.categoryAsPathVariable = categoryAsPathVariable;
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) return true;
    if (!(o instanceof Category)) return false;
    Category categoryObj = (Category) o;
    return Objects.equals(this.id, categoryObj.id) &&
           Objects.equals(this.category, categoryObj.category) &&
           Objects.equals(this.categoryAsPathVariable, categoryObj.categoryAsPathVariable);
  }

  @Override
  public int hashCode() {
    return Objects.hash(this.id, this.category, this.categoryAsPathVariable);
  }

  @Override
  public String toString() {
    return "Category{" +
             "id=" + this.id + ", " +
             "category=\'" + this.category + "\', " +
             "categoryAsPathVariable=\'" + this.categoryAsPathVariable + "\'" +
           "}";
  }
}
