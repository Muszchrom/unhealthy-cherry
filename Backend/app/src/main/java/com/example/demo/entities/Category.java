package com.example.demo.entities;

import java.util.List;
import java.util.Objects;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;

@Entity
@Table(name="category")
public class Category {

  @Column(name="id", nullable=false, unique=true) 
  @Id @GeneratedValue(strategy=GenerationType.IDENTITY)
  private Long id;

  // length=255 is the default value, that's why it's ommited
  @Column(name="category", nullable=false, unique=true)
  private String category;

  @Column(name="category_as_path_variable", nullable=false, unique=true)
  private String categoryAsPathVariable;

  @OneToMany(mappedBy="category", cascade=CascadeType.ALL, orphanRemoval=true)
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

  public boolean isCategoryAsPathVariableValid() {
    if (this.categoryAsPathVariable == null) {
      return false;
    }
    Pattern pattern = Pattern.compile("^[a-z0-9_-]+$");
    Matcher matcher = pattern.matcher(this.categoryAsPathVariable);
    return matcher.matches();
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
