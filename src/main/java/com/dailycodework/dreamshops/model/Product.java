package com.dailycodework.dreamshops.model;

import java.math.BigDecimal;
import java.util.List;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;
import jakarta.persistence.CascadeType;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Table(name = "products")
@Entity
public class Product {
  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long id;
  private String name;
  private String brand;
  private BigDecimal price;
  private int inventory;
  private String description;

  // The cascade attribute is set to CascadeType.ALL, which means that all
  // persistence operations (PERSIST, MERGE, REMOVE, REFRESH, DETACH) will be
  // cascaded to the associated Category entity.
  @ManyToOne(cascade = CascadeType.ALL)
  @JoinColumn(name = "category_id")
  @JsonIgnoreProperties("products")
  private Category category;

  /**
   * A list of images associated with the product.
   * This field is mapped by the "product" field in the Image entity.
   * It uses a one-to-many relationship, meaning a product can have multiple
   * images.
   * if an image is removed from the product's list, it will also be removed from
   * the
   * database.
   */
  @OneToMany(mappedBy = "product", cascade = CascadeType.ALL, orphanRemoval = true)
  @JsonIgnoreProperties("product")
  private List<Image> images;

  public Product(String name, String brand, BigDecimal price, int inventory, String description, Category category) {
    this.name = name;
    this.brand = brand;
    this.price = price;
    this.inventory = inventory;
    this.description = description;
    this.category = category;
  }
}
