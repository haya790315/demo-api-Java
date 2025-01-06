package com.dailycodework.dreamshops.controller;

import java.util.List;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.dailycodework.dreamshops.exceptions.CategoryNotFoundException;
import com.dailycodework.dreamshops.model.Category;
import com.dailycodework.dreamshops.response.ApiResponse;
import com.dailycodework.dreamshops.service.category.ICategoryService;

import lombok.RequiredArgsConstructor;

// Indicates that this class is a REST controller and will handle HTTP requests.
@RestController
// ("${api.prefix}/images") - Maps HTTP requests to /images endpoint, with a
// prefix defined in the application properties.
@RequestMapping("${api.prefix}/categories")
// Generates a constructor with required arguments (final fields) for dependency
// injection.
@RequiredArgsConstructor
public class CategoryController {
  private final ICategoryService categoryService;

  @GetMapping("/all")
  public ResponseEntity<ApiResponse> getAllCategories() {
    try {
      List<Category> categories = categoryService.getAllCategories();
      return ResponseEntity.ok(new ApiResponse("Found! 🥳", categories));
    } catch (Exception e) {
      return ResponseEntity.internalServerError()
          .body(new ApiResponse("Failed😵‍💫", HttpStatus.INTERNAL_SERVER_ERROR));
    }
  }

  @PostMapping("/add")
  public ResponseEntity<ApiResponse> addCategory(@RequestBody Category name) {
    try {
      Category theCategory = categoryService.addCategory(name);
      return ResponseEntity.ok(new ApiResponse("Add Success 🥳", theCategory));
    } catch (CategoryNotFoundException e) {
      return ResponseEntity.status(HttpStatus.CONFLICT).body(new ApiResponse(e.getMessage(), null));
    }
  }

  @GetMapping("/category/id/{id}")
  public ResponseEntity<ApiResponse> getCategoryById(@PathVariable Long id) {
    try {
      Category theCategory = categoryService.getCategoryById(id);
      return ResponseEntity.ok(new ApiResponse("Found", theCategory));
    } catch (CategoryNotFoundException e) {
      return ResponseEntity.status(HttpStatus.NOT_FOUND).body(new ApiResponse(e.getMessage(), null));
    }
  }

  @GetMapping("/category/name/{name}")
  public ResponseEntity<ApiResponse> getCategoryByName(@PathVariable String name) {
    try {
      Category theCategory = categoryService.getCategoryByName(name);
      return ResponseEntity.ok(new ApiResponse("Found", theCategory));
    } catch (CategoryNotFoundException e) {
      return ResponseEntity.status(HttpStatus.NOT_FOUND).body(new ApiResponse(e.getMessage(), null));
    }
  }

  @DeleteMapping("/category/{id}/delete")
  public ResponseEntity<ApiResponse> deleteCategoryById(@PathVariable Long id) {
    try {
      categoryService.deleteCategory(id);
      return ResponseEntity.ok(new ApiResponse("Found", null));
    } catch (CategoryNotFoundException e) {
      return ResponseEntity.status(HttpStatus.NOT_FOUND).body(new ApiResponse(e.getMessage(), null));
    }
  }
}
