package com.dailycodework.dreamshops.request;

import java.math.BigDecimal;

import com.dailycodework.dreamshops.model.Category;

import lombok.Data;

/**
 * Represents a request to add a new product.
 * This class contains the necessary details required to create a new product entry.
 * 
 * Fields:
 * - name: The name of the product.
 * - brand: The brand of the product.
 * - price: The price of the product.
 * - inventory: The inventory count of the product.
 * - description: A brief description of the product.
 * - category: The category to which the product belongs.
 */
@Data
public class AddProductRequest {
  private String name;
  private String brand;
  private BigDecimal price;
  private int inventory;
  private String description;
  private Category category;
}
