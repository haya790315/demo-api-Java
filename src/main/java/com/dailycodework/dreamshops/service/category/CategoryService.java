package com.dailycodework.dreamshops.service.category;

import java.util.List;
import java.util.Optional;

import org.springframework.stereotype.Service;

import com.dailycodework.dreamshops.exceptions.CategoryNotFoundException;
import com.dailycodework.dreamshops.model.Category;
import com.dailycodework.dreamshops.repository.CategoryRepository;
import com.dailycodework.dreamshops.request.CategoryRequest;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class CategoryService implements ICategoryService {

  private final CategoryRepository categoryRepository;

  @Override
  public Category getCategoryById(Long id) {
    return categoryRepository.findById(id)
        .orElseThrow(() -> new CategoryNotFoundException("Category: " + id + " not found"));
  }

  @Override
  public Category getCategoryByName(String name) {
    return Optional.ofNullable(categoryRepository.findByName(name))
        .orElseThrow(() -> new CategoryNotFoundException("Category: " + name + " not found"));
  }

  @Override
  public List<Category> getAllCategories() {
    return categoryRepository.findAll();
  }

  @Override
  public Category addCategory(Category category) {
    return Optional.of(category).filter(c -> !categoryRepository.existsByName(c.getName()))
        .map(c -> categoryRepository.save(category))
        .orElseThrow(() -> new CategoryNotFoundException(category.getName()));
  }

  @Override
  public Category updateCategory(CategoryRequest request, Long id) {
    return Optional.ofNullable(getCategoryById(id)).map(old -> {
      old.setName(request.getName());
      return categoryRepository.save(old);
    }).orElseThrow(() -> new CategoryNotFoundException("category: " + id + " not found"));
  }

  @Override
  public void deleteCategory(Long id) {
    categoryRepository.findById(id).ifPresentOrElse(categoryRepository::delete,
        () -> new CategoryNotFoundException("category: " + id + " not found"));
  }

}
