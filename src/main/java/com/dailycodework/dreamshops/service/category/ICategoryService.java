package com.dailycodework.dreamshops.service.category;

import java.util.List;

import com.dailycodework.dreamshops.model.Category;
import com.dailycodework.dreamshops.request.CategoryRequest;

public interface ICategoryService {
  Category getCategoryById(Long id);

  Category getCategoryByName(String name);

  List<Category> getAllCategories();

  Category addCategory(Category category);

  Category updateCategory(CategoryRequest request, Long id);

  void deleteCategory(Long id);

}
