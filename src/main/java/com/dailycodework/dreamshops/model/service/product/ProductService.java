package com.dailycodework.dreamshops.model.service.product;

import java.util.List;

import com.dailycodework.dreamshops.exceptions.ProductNotFoundException;
import com.dailycodework.dreamshops.model.Product;
import com.dailycodework.dreamshops.repository.ProductRepository;

/**
 * Service class for managing products.
 * Implements the IProductService interface to provide various product-related
 * operations.
 */
public class ProductService implements IProductService {
  private ProductRepository productRepository;

  @Override
  public Product addProduct(Product product) {
    // TODO Auto-generated method stub
    throw new UnsupportedOperationException("Unimplemented method 'addProduct'");
  }

  @Override
  public Product getProductById(Long id) {
    return productRepository.findById(id)
        .orElseThrow(() -> new ProductNotFoundException("Product with id " + id + " not found"));

  }

  @Override
  public void deleteProductById(Long id) {
    // productRepository.findById(id).ifPresent(productRepository::delete);
    // shorthand for the following:
    productRepository.findById(id).ifPresentOrElse(product -> productRepository.delete(product),
        () -> {
          throw new ProductNotFoundException("Product with id " + id + " not found");
        });
  }

  @Override
  public void updateProduct(Product product, Long productId) {
    // TODO Auto-generated method stub
    throw new UnsupportedOperationException("Unimplemented method 'updateProduct'");
  }

  @Override
  public List<Product> getAllProducts() {
    return productRepository.findAll();
  }

  @Override
  public List<Product> getProductsByCategory(String category) {
    return productRepository.findByCategoryName(category);
  }

  @Override
  public List<Product> getProductsByBrand(String brand) {
    return productRepository.findByBrand(brand);
  }

  @Override
  public List<Product> getProductByCategoryAndBrand(String category, String brand) {
    return productRepository.findByCategoryNameAndBrand(category, brand);
  }

  @Override
  public List<Product> getProductByName(String name) {
    // TODO Auto-generated method stub
    throw new UnsupportedOperationException("Unimplemented method 'getProductByName'");
  }

  @Override
  public List<Product> getProductByBrandAndName(String brand, String name) {
    // TODO Auto-generated method stub
    throw new UnsupportedOperationException("Unimplemented method 'getProductByBrandAndName'");
  }

  @Override
  public Long CountProductsByBrandAndName(String brand, String name) {
    // TODO Auto-generated method stub
    throw new UnsupportedOperationException("Unimplemented method 'CountProductsByBrandAndName'");
  }

}
