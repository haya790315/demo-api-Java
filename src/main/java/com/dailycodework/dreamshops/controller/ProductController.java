package com.dailycodework.dreamshops.controller;

import java.util.List;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.dailycodework.dreamshops.exceptions.AlreadyExistsException;
import com.dailycodework.dreamshops.exceptions.ProductNotFoundException;
import com.dailycodework.dreamshops.model.Product;
import com.dailycodework.dreamshops.request.AddProductRequest;
import com.dailycodework.dreamshops.request.ProductUpdateRequest;
import com.dailycodework.dreamshops.response.ApiResponse;
import com.dailycodework.dreamshops.service.product.IProductService;

import lombok.RequiredArgsConstructor;

import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.PutMapping;

// Indicates that this class is a REST controller and will handle HTTP requests.
@RestController
// ("${api.prefix}/images") - Maps HTTP requests to /images endpoint, with a
// prefix defined in the application properties.
@RequestMapping("${api.prefix}/products")
// Generates a constructor with required arguments (final fields) for dependency
// injection.
@RequiredArgsConstructor
public class ProductController {
  final private IProductService productService;

  @GetMapping("/all")
  public ResponseEntity<ApiResponse> getAllProducts() {
    try {
      List<Product> products = productService.getAllProducts();
      return ResponseEntity.ok(new ApiResponse("Yes! Success 🥳", products));
    } catch (Exception e) {
      return ResponseEntity.internalServerError().body(new ApiResponse("Nope! Failed 😵‍💫", null));
    }
  }

  @GetMapping("/product/{productId}/id")
  public ResponseEntity<ApiResponse> getProductById(@PathVariable("productId") Long id) {
    try {
      Product product = productService.getProductById(id);
      return ResponseEntity.ok(new ApiResponse("Yes! success 🥳", product));
    } catch (ProductNotFoundException e) {
      return ResponseEntity.status(HttpStatus.NOT_FOUND).body(new ApiResponse("Nope!, not Found 😵‍💫", null));
    } catch (Exception e) {
      return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
          .body(new ApiResponse(e.getMessage(), null));
    }
  }

  @GetMapping("/product/{name}/name")
  public ResponseEntity<ApiResponse> getProductByName(@PathVariable String name) {
    try {
      List<Product> products = productService.getProductByName(name);
      if (products.isEmpty()) {
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(new ApiResponse("Nope!, not Found 😵‍💫", null));
      }
      return ResponseEntity.ok(new ApiResponse("Yes! success 🥳", products));
    } catch (Exception e) {
      return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
          .body(new ApiResponse("Nope!, not Found 😵‍💫", null));
    }
  }

  @PostMapping("/add")
  public ResponseEntity<ApiResponse> addProduct(@RequestBody AddProductRequest product) {
    try {
      Product theProduct = productService.addProduct(product);
      return ResponseEntity.ok(new ApiResponse("Add product success! 🥳", theProduct));
    } catch (AlreadyExistsException e) {
      return ResponseEntity.status(HttpStatus.CONFLICT).body(new ApiResponse(e.getMessage(), null));
    }
  }

  @PutMapping("/product/{productId}")
  public ResponseEntity<ApiResponse> updateProduct(@PathVariable Long productId,
      @RequestBody ProductUpdateRequest updateProduct) {
    try {
      Product product = productService.updateProduct(updateProduct, productId);
      return ResponseEntity.ok(new ApiResponse("Update product success!", product));
    } catch (ProductNotFoundException e) {
      return ResponseEntity.status(HttpStatus.NOT_FOUND)
          .body(new ApiResponse("Failed not found !😵‍💫", null));
    } catch (Exception e) {
      return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
          .body(new ApiResponse(e.getMessage(), null));
    }
  }

  @DeleteMapping("/product/{productId}/delete")
  public ResponseEntity<ApiResponse> deleteProduct(@PathVariable Long productId) {
    try {
      productService.deleteProductById(productId);
      return ResponseEntity.ok(new ApiResponse("Delete product success! 🥳", productId));
    } catch (ProductNotFoundException e) {
      return ResponseEntity.status(HttpStatus.NOT_FOUND).body(new ApiResponse(e.getMessage(), null));
    }
  }

  @GetMapping("/product/by/brand-and-name")
  public ResponseEntity<ApiResponse> getProductByBrandAndName(@RequestParam String brandName,
      @RequestParam String productName) {
    try {
      List<Product> products = productService.getProductByBrandAndName(brandName, productName);
      if (products.isEmpty()) {
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(new ApiResponse("not found product! 😵‍💫", null));
      }
      return ResponseEntity.ok(new ApiResponse("Success ! 🥳", products));
    } catch (Exception e) {
      return ResponseEntity.internalServerError().body(new ApiResponse(e.getMessage(), null));
    }
  }

  @GetMapping("/product/by/category-and-brand")
  public ResponseEntity<ApiResponse> getProductByCategoryAndBrand(@RequestParam String category,
      @RequestParam String brand) {
    try {
      List<Product> products = productService.getProductByCategoryAndBrand(category, brand);
      if (products.isEmpty()) {
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(new ApiResponse("not found product! 😵‍💫", null));
      }
      return ResponseEntity.ok(new ApiResponse("Success ! 🥳", products));
    } catch (Exception e) {
      return ResponseEntity.internalServerError().body(new ApiResponse(e.getMessage(), null));
    }
  }

  @GetMapping("/product/by-brand")
  public ResponseEntity<ApiResponse> getProductsByBrand(@RequestParam String brand) {
    try {
      List<Product> products = productService.getProductsByBrand(brand);
      if (products.isEmpty()) {
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(new ApiResponse("not found product! 😵‍💫", null));
      }
      return ResponseEntity.ok(new ApiResponse("Success ! 🥳", products));
    } catch (Exception e) {
      return ResponseEntity.internalServerError().body(new ApiResponse(e.getMessage(), null));
    }
  }

  @GetMapping("/product/{category}/all")
  public ResponseEntity<ApiResponse> getProductsByCategory(@RequestParam String category) {
    try {
      List<Product> products = productService.getProductsByCategory(category);
      if (products.isEmpty()) {
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(new ApiResponse("not found product! 😵‍💫", null));
      }
      return ResponseEntity.ok(new ApiResponse("Success ! 🥳", products));
    } catch (Exception e) {
      return ResponseEntity.internalServerError().body(new ApiResponse(e.getMessage(), null));
    }
  }
}
