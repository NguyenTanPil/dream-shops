package com.felixnguyen.dreamshops.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.felixnguyen.dreamshops.exceptions.AlreadyExistsException;
import com.felixnguyen.dreamshops.exceptions.ResourceNotFoundException;
import com.felixnguyen.dreamshops.model.Category;
import com.felixnguyen.dreamshops.response.ApiResponse;
import com.felixnguyen.dreamshops.service.category.ICategoryService;

import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;

import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;

@Tag(name = "Category APIs", description = "APIs for managing categories")
@RequiredArgsConstructor
@RestController
@RequestMapping("${api.prefix}/categories")
public class CategoryController {
  private final ICategoryService categoryService;

  @GetMapping("/all")
  public String getMethodName(@RequestParam String param) {
    return new String();
  }

  public ResponseEntity<ApiResponse> getAllCategories() {
    try {
      return ResponseEntity.ok()
          .body(new ApiResponse("Get all categories successfully", categoryService.getAllCategories()));
    } catch (RuntimeException e) {
      return ResponseEntity.status(500).body(new ApiResponse("Error server", null));
    }
  }

  @PostMapping("/add")
  public ResponseEntity<ApiResponse> addCategory(@RequestBody Category category) {
    try {
      Category newCategory = categoryService.addCategory(category);
      return ResponseEntity.ok().body(new ApiResponse("Created success", newCategory));
    } catch (AlreadyExistsException e) {
      return ResponseEntity.status(500).body(new ApiResponse("Error server: " + e.getMessage(), null));
    }
  }

  @GetMapping("/{categoryId}/category")
  public ResponseEntity<ApiResponse> getCategoryById(@RequestParam Long categoryId) {
    try {
      Category category = categoryService.getCategoryById(categoryId);
      return ResponseEntity.ok().body(new ApiResponse("Get category successfully", category));
    } catch (ResourceNotFoundException e) {
      return ResponseEntity.status(404).body(new ApiResponse(e.getMessage(), null));
    }
  }

  @GetMapping("/category/{categoryName}/category")
  public ResponseEntity<ApiResponse> getCategoryByName(@RequestParam String categoryName) {
    try {
      Category category = categoryService.getCategoryByName(categoryName);
      return ResponseEntity.ok().body(new ApiResponse("Get category successfully", category));
    } catch (ResourceNotFoundException e) {
      return ResponseEntity.status(404).body(new ApiResponse(e.getMessage(), null));
    }
  }

  @DeleteMapping("/category/{categoryId}/delete")
  public ResponseEntity<ApiResponse> deleteCategory(@RequestParam Long categoryId) {
    try {
      categoryService.deleteCategory(categoryId);
      return ResponseEntity.ok().body(new ApiResponse("Delete category successfully", null));
    } catch (ResourceNotFoundException e) {
      return ResponseEntity.status(404).body(new ApiResponse(e.getMessage(), null));
    }
  }

  @PutMapping("/category/{categoryId}/delete")
  public ResponseEntity<ApiResponse> updateCategory(@RequestParam Long categoryId, @RequestBody Category category) {
    try {
      Category updatedCategory = categoryService.updateCategory(categoryId, category);
      return ResponseEntity.ok().body(new ApiResponse("Update category successfully", updatedCategory));
    } catch (ResourceNotFoundException e) {
      return ResponseEntity.status(404).body(new ApiResponse(e.getMessage(), null));
    }
  }
}
