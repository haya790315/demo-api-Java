package com.dailycodework.dreamshops.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.dailycodework.dreamshops.model.Category;

/**
 * Repository interface for Category entities.
 * Extends JpaRepository to provide CRUD operations and additional query
 * methods.
 */
public interface CategoryRepository extends JpaRepository<Category, Long> {

  /**
   * Finds a Category entity by its name.
   *
   * @param name the name of the category
   * @return the Category entity with the given name, or null if not found
   */
  Category findByName(String name);

  /**
   * Checks if a category with the given name exists.
   * 
   * @param name the name of the category
   * @return true if a category with the given name exists, false otherwise
   */
  boolean existsByName(String name);
}
