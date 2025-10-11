package com.felixnguyen.dreamshops.service.category;

import java.util.List;

import com.felixnguyen.dreamshops.dto.CategoryDto;
import com.felixnguyen.dreamshops.model.Category;

public interface ICategoryService {
  CategoryDto getCategoryById(Long id);

  CategoryDto getCategoryByName(String name);

  List<CategoryDto> getAllCategories();

  CategoryDto addCategory(Category category);

  CategoryDto updateCategory(Long id, Category category);

  void deleteCategory(Long id);
}
