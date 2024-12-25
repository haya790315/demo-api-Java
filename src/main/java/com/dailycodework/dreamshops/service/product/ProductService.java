package com.dailycodework.dreamshops.service.product;

import java.util.List;
import java.util.Optional;

import org.springframework.stereotype.Service;

import com.dailycodework.dreamshops.exceptions.ProductNotFoundException;
import com.dailycodework.dreamshops.model.Category;
import com.dailycodework.dreamshops.model.Product;
import com.dailycodework.dreamshops.repository.CategoryRepository;
import com.dailycodework.dreamshops.repository.ProductRepository;
import com.dailycodework.dreamshops.request.AddProductRequest;
import com.dailycodework.dreamshops.request.ProductUpdateRequest;

import lombok.RequiredArgsConstructor;

/**
 * Service class for managing products.
 * Implements the IProductService interface to provide various product-related
 * operations.
 */
@Service
@RequiredArgsConstructor
public class ProductService implements IProductService {
  // Repositories for accessing product and category data from the database
  private final ProductRepository productRepository;
  private final CategoryRepository categoryRepository;

  @Override
  public Product addProduct(AddProductRequest request) {
    // Extract the category name from the request
    String categoryName = request.getCategory().getName();

    // Find the category by name, or create a new one if it doesn't exist
    Category category = Optional.ofNullable(categoryRepository.findByName(categoryName)).orElseGet(() -> {
      Category newCategory = new Category(categoryName);
      return categoryRepository.save(newCategory);
    });

    // Set the category in the request
    request.setCategory(category);

    // Save and return the new product
    return productRepository.save(createProduct(request));
  }

  // Helper method to create a Product entity from the request
  private Product createProduct(AddProductRequest request) {
    // Build and return a new Product entity from the request
    return Product.builder()
        .name(request.getName())
        .brand(request.getBrand())
        .price(request.getPrice())
        .inventory(request.getInventory())
        .description(request.getDescription())
        .category(request.getCategory())
        .build();
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
  public Product updateProduct(ProductUpdateRequest request, Long productId) {
    // Find the existing product by id
    return productRepository.findById(productId)
        // If the product is found, update it with the new values from the request
        .map(product -> updateExistingProduct(product, request))
        // Save the updated product to the repository
        .map(productRepository::save)
        // If the product is not found, throw a ProductNotFoundException
        .orElseThrow(() -> new ProductNotFoundException("Product with id " + productId + " not found"));
  }

  // Helper method to update a Product entity from the request
  private Product updateExistingProduct(Product existingProduct, ProductUpdateRequest request) {
    // Update the product fields with the new values
    existingProduct.setName(request.getName());
    existingProduct.setBrand(request.getBrand());
    existingProduct.setPrice(request.getPrice());
    existingProduct.setInventory(request.getInventory());
    existingProduct.setDescription(request.getDescription());

    Category category = Optional.ofNullable(categoryRepository.findByName(request.getCategory().getName()))
        .orElseThrow(
            () -> new ProductNotFoundException("Category with name " + request.getCategory().getName() + " not found"));
    existingProduct.setCategory(category);

    return existingProduct;
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
    return productRepository.findByName(name);
  }

  @Override
  public List<Product> getProductByBrandAndName(String brand, String name) {
    return productRepository.findByBrandAndName(brand, name);
  }

  @Override
  public Long CountProductsByBrandAndName(String brand, String name) {
    return productRepository.countByBrandAndName(brand, name);
  }

}
