package com.felixnguyen.dreamshops.controller;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.felixnguyen.dreamshops.dto.ProductDto;
import com.felixnguyen.dreamshops.exceptions.AlreadyExistsException;
import com.felixnguyen.dreamshops.exceptions.ProductNotFoundException;
import com.felixnguyen.dreamshops.exceptions.ResourceNotFoundException;
import com.felixnguyen.dreamshops.request.AddProductRequest;
import com.felixnguyen.dreamshops.request.ProductUpdateRequest;
import com.felixnguyen.dreamshops.response.ApiResponse;
import com.felixnguyen.dreamshops.service.product.IProductService;

import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;

import java.util.List;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;

@Tag(name = "Product APIs", description = "APIs for managing products")
@RequiredArgsConstructor
@RestController
@RequestMapping("${api.prefix}/products")
public class ProductController {
  private final IProductService productService;

  @GetMapping("/all")
  public ResponseEntity<ApiResponse> getAllProducts() {
    List<ProductDto> products = productService.getAllProducts();
    return ResponseEntity.ok(new ApiResponse("Products retrieved successfully", products));
  }

  @GetMapping("/{productId}/product")
  public ResponseEntity<ApiResponse> getProductById(@PathVariable("productId") Long id) {
    try {
      ProductDto product = productService.getProductById(id);
      return ResponseEntity.ok(new ApiResponse("Product retrieved successfully", product));
    } catch (ResourceNotFoundException e) {
      return ResponseEntity.status(HttpStatus.NOT_FOUND).body(new ApiResponse(e.getMessage(), null));
    }
  }

  @PreAuthorize("hasRole('ROLE_ADMIN')")
  @PostMapping("/add")
  public ResponseEntity<ApiResponse> addProduct(@RequestBody AddProductRequest product) {
    try {
      ProductDto createdProduct = productService.addProduct(product);
      return ResponseEntity.status(HttpStatus.CREATED)
          .body(new ApiResponse("Product created successfully", createdProduct));
    } catch (AlreadyExistsException e) {
      return ResponseEntity.status(HttpStatus.CONFLICT).body(new ApiResponse(e.getMessage(), null));
    }
  }

  @PreAuthorize("hasRole('ROLE_ADMIN')")
  @PutMapping("/{id}/update")
  public ResponseEntity<ApiResponse> updateProduct(@PathVariable Long id, @RequestBody ProductUpdateRequest product) {
    try {
      ProductDto updatedProduct = productService.updateProduct(id, product);
      return ResponseEntity.ok(new ApiResponse("Updated product", updatedProduct));
    } catch (ProductNotFoundException e) {
      return ResponseEntity.status(HttpStatus.NOT_FOUND).body(new ApiResponse(e.getMessage(), null));
    }
  }

  @PreAuthorize("hasRole('ROLE_ADMIN')")
  @DeleteMapping("/{id}/delete")
  public ResponseEntity<ApiResponse> deleteProduct(@PathVariable Long id) {
    try {
      productService.deleteProduct(id);
      return ResponseEntity.ok(new ApiResponse("Product deleted successfully", null));
    } catch (ProductNotFoundException e) {
      return ResponseEntity.status(HttpStatus.NOT_FOUND).body(new ApiResponse(e.getMessage(), null));
    }
  }

  @GetMapping("/by-category-and-name")
  public ResponseEntity<ApiResponse> getProductByCategoryAndBrand(@RequestParam String categoryName,
      @RequestParam String brandName) {
    try {
      List<ProductDto> products = productService.getProductsByCategoryAndBrand(categoryName, brandName);
      if (products.isEmpty()) {
        return ResponseEntity.status(HttpStatus.NOT_FOUND)
            .body(
                new ApiResponse("No products found for category: " + categoryName + " and brand: " + brandName, null));
      }
      return ResponseEntity.ok().body(new ApiResponse("Products retrieved successfully", products));
    } catch (Exception e) {
      return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(new ApiResponse(e.getMessage(), null));
    }
  }

  @GetMapping("/by-brand-and-name")
  public ResponseEntity<ApiResponse> getProductByBrandAndName(@RequestParam String brandName,
      @RequestParam String productName) {
    try {
      List<ProductDto> products = productService.getProductsByBrandAndName(brandName, productName);
      if (products.isEmpty()) {
        return ResponseEntity.status(HttpStatus.NOT_FOUND)
            .body(new ApiResponse("No products found for brand: " + brandName + " and name: " + productName, null));
      }
      return ResponseEntity.ok().body(new ApiResponse("Products retrieved successfully", products));
    } catch (Exception e) {
      return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(new ApiResponse(e.getMessage(), null));
    }
  }

  @GetMapping("/by-name")
  public ResponseEntity<ApiResponse> getProductByName(@RequestParam String productName) {
    try {
      List<ProductDto> products = productService.getProductsByName(productName);
      if (products.isEmpty()) {
        return ResponseEntity.status(HttpStatus.NOT_FOUND)
            .body(new ApiResponse("No products found for name: " + productName, null));
      }
      return ResponseEntity.ok().body(new ApiResponse("Products retrieved successfully", products));
    } catch (Exception e) {
      return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(new ApiResponse(e.getMessage(), null));
    }
  }

  @GetMapping("/by-brand")
  public ResponseEntity<ApiResponse> getProductByBrand(@RequestParam String brandName) {
    try {
      List<ProductDto> products = productService.getProductsByBrand(brandName);
      if (products.isEmpty()) {
        return ResponseEntity.status(HttpStatus.NOT_FOUND)
            .body(new ApiResponse("No products found for brand: " + brandName, null));
      }
      return ResponseEntity.ok().body(new ApiResponse("Products retrieved successfully", products));
    } catch (Exception e) {
      return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(new ApiResponse(e.getMessage(), null));
    }
  }

  @GetMapping("/by-category")
  public ResponseEntity<ApiResponse> getProductByCategory(@RequestParam String categoryName) {
    try {
      List<ProductDto> products = productService.getProductsByCategory(categoryName);
      if (products.isEmpty()) {
        return ResponseEntity.status(HttpStatus.NOT_FOUND)
            .body(new ApiResponse("No products found for category: " + categoryName, null));
      }
      return ResponseEntity.ok().body(new ApiResponse("Products retrieved successfully", products));
    } catch (Exception e) {
      return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(new ApiResponse(e.getMessage(), null));
    }
  }
}
