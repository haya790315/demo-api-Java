package com.dailycodework.dreamshops.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.dailycodework.dreamshops.model.Product;

public interface ProductRepository extends JpaRepository<Product, Long> {

  List<Product> findByCategoryName(String category);

  List<Product> findByBrand(String brand);

  List<Product> findByCategoryNameAndBrand(String category, String brand);

}