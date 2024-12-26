package com.dailycodework.dreamshops.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.dailycodework.dreamshops.model.Product;

/**
 * Repository interface for managing {@link Product} entities.
 * Extends {@link JpaRepository} to provide CRUD operations and custom query methods.
 */
public interface ProductRepository extends JpaRepository<Product, Long> {

  /**
   * Finds a list of products by the given category name.
   *
   * @param category the name of the category
   * @return a list of products in the specified category
   */
  List<Product> findByCategoryName(String category);

  /**
   * Finds a list of products by the given brand.
   *
   * @param brand the name of the brand
   * @return a list of products of the specified brand
   */
  List<Product> findByBrand(String brand);

  /**
   * Finds a list of products by the given category name and brand.
   *
   * @param category the name of the category
   * @param brand the name of the brand
   * @return a list of products in the specified category and brand
   */
  List<Product> findByCategoryNameAndBrand(String category, String brand);

  /**
   * Finds a list of products by the given name.
   *
   * @param name the name of the product
   * @return a list of products with the specified name
   */
  List<Product> findByName(String name);

  /**
   * Finds a list of products by the given brand and name.
   *
   * @param brand the name of the brand
   * @param name the name of the product
   * @return a list of products with the specified brand and name
   */
  List<Product> findByBrandAndName(String brand, String name);

  /**
   * Counts the number of products by the given brand and name.
   *
   * @param brand the name of the brand
   * @param name the name of the product
   * @return the number of products with the specified brand and name
   */
  Long countByBrandAndName(String brand, String name);

}
