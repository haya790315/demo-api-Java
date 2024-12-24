package com.dailycodework.dreamshops.model.service.product;

import java.util.List;

import com.dailycodework.dreamshops.model.Product;

/**
 * Interface for Product Service operations.
 * Provides methods to perform CRUD operations and queries on products.
 */
public interface IProductService {
  Product addProduct(Product product);

  Product getProductById(Long id);

  void deleteProductById(Long id);

  void updateProduct(Product product, Long productId);

  List<Product> getAllProducts();

  List<Product> getProductsByCategory(String category);

  List<Product> getProductsByBrand(String brand);

  List<Product> getProductByCategoryAndBrand(String category, String brand);

  List<Product> getProductByName(String name);

  List<Product> getProductByBrandAndName(String brand, String name);

  Long CountProductsByBrandAndName(String brand, String name);

}
