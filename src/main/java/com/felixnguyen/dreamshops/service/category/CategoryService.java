package com.felixnguyen.dreamshops.service.category;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;

import com.felixnguyen.dreamshops.dto.CategoryDto;
import com.felixnguyen.dreamshops.exceptions.AlreadyExistsException;
import com.felixnguyen.dreamshops.exceptions.ResourceNotFoundException;
import com.felixnguyen.dreamshops.model.Category;
import com.felixnguyen.dreamshops.repository.CategoryRepository;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class CategoryService implements ICategoryService {
  private final CategoryRepository categoryRepository;
  private final ModelMapper modelMapper;

  private CategoryDto convertToDto(Category category) {
    return modelMapper.map(category, CategoryDto.class);
  }

  @Override
  public CategoryDto getCategoryById(Long id) {
    return categoryRepository.findById(id).map(this::convertToDto)
        .orElseThrow(() -> new ResourceNotFoundException("Category not found with id: " + id));
  }

  @Override
  public CategoryDto getCategoryByName(String name) {
    return categoryRepository.findByName(name).map(this::convertToDto)
        .orElseThrow(() -> new ResourceNotFoundException("Category not found with name: " + name));
  }

  @Override
  public List<CategoryDto> getAllCategories() {
    return categoryRepository.findAll().stream().map(this::convertToDto).collect(Collectors.toList());
  }

  @Override
  public CategoryDto addCategory(Category category) {
    return Optional.ofNullable(
        category).filter(c -> !categoryRepository.existsByName(c.getName()))
        .map(
            categoryRepository::save)
        .map(this::convertToDto)
        .orElseThrow(() -> new AlreadyExistsException("Category already exists with name: " + category.getName()));
  }

  @Override
  public CategoryDto updateCategory(Long id, Category category) {
    return categoryRepository.findById(id).map(oldCategory -> {
      oldCategory.setName(category.getName());
      return categoryRepository.save(oldCategory);
    }).map(this::convertToDto).orElseThrow(() -> new ResourceNotFoundException("Category not found with id: " + id));
  }

  @Override
  public void deleteCategory(Long id) {
    categoryRepository.findById(id).ifPresentOrElse(categoryRepository::delete, () -> {
      throw new ResourceNotFoundException("Category not found with id: " + id);
    });
  }
}