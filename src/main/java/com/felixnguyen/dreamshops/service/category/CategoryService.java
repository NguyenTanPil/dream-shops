package com.felixnguyen.dreamshops.service.category;

import java.util.List;
import java.util.Optional;

import org.springframework.stereotype.Service;

import com.felixnguyen.dreamshops.exceptions.AlreadyExistsException;
import com.felixnguyen.dreamshops.exceptions.ResourceNotFoundException;
import com.felixnguyen.dreamshops.model.Category;
import com.felixnguyen.dreamshops.repository.CategoryRepository;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class CategoryService implements ICategoryService {
  private final CategoryRepository categoryRepository;

  @Override
  public Category getCategoryById(Long id) {
    return categoryRepository.findById(id)
        .orElseThrow(() -> new ResourceNotFoundException("Category not found with id: " + id));
  }

  @Override
  public Category getCategoryByName(String name) {
    return categoryRepository.findByName(name)
        .orElseThrow(() -> new ResourceNotFoundException("Category not found with name: " + name));
  }

  @Override
  public List<Category> getAllCategories() {
    return categoryRepository.findAll();
  }

  @Override
  public Category addCategory(Category category) {
    return Optional.ofNullable(
        category).filter(c -> !categoryRepository.existsByName(c.getName()))
        .map(
            categoryRepository::save)
        .orElseThrow(() -> new AlreadyExistsException("Category already exists with name: " + category.getName()));
  }

  @Override
  public Category updateCategory(Long id, Category category) {
    return Optional.ofNullable(getCategoryById(id)).map(oldCategory -> {
      oldCategory.setName(category.getName());
      return categoryRepository.save(oldCategory);
    }).orElseThrow(() -> new ResourceNotFoundException("Category not found with id: " + id));
  }

  @Override
  public void deleteCategory(Long id) {
    categoryRepository.findById(id).ifPresentOrElse(categoryRepository::delete, () -> {
      throw new ResourceNotFoundException("Category not found with id: " + id);
    });
  }
}