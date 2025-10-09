package com.felixnguyen.dreamshops.service.product;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.felixnguyen.dreamshops.dto.CategoryDto;
import com.felixnguyen.dreamshops.dto.ImageDto;
import com.felixnguyen.dreamshops.dto.ProductDto;
import com.felixnguyen.dreamshops.exceptions.ProductNotFoundException;
import com.felixnguyen.dreamshops.model.Category;
import com.felixnguyen.dreamshops.model.Product;
import com.felixnguyen.dreamshops.repository.CategoryRepository;
import com.felixnguyen.dreamshops.repository.ProductRepository;
import com.felixnguyen.dreamshops.request.AddProductRequest;
import com.felixnguyen.dreamshops.request.ProductUpdateRequest;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class ProductService implements IProductService {
  private final ProductRepository productRepository;
  private final CategoryRepository categoryRepository;

  private Product createProduct(AddProductRequest request, Category category) {
    return new Product(
        request.getName(),
        request.getBrand(),
        request.getPrice(),
        request.getInventory(),
        request.getDescription(),
        category);
  }

  private ProductDto convertToDto(Product product) {
    ProductDto dto = new ProductDto();
    dto.setId(product.getId());
    dto.setName(product.getName());
    dto.setBrand(product.getBrand());
    dto.setPrice(product.getPrice());
    dto.setInventory(product.getInventory());
    dto.setDescription(product.getDescription());
    CategoryDto category = new CategoryDto();
    category.setId(product.getCategory().getId());
    category.setName(product.getCategory().getName());

    dto.setCategory(category);
    dto.setImages(product.getImages().stream()
        .map(image -> {
          var imageDto = new ImageDto();
          imageDto.setImageId(image.getId());
          imageDto.setDownloadUrl(image.getDownloadUrl());
          imageDto.setImageName(image.getFileName());
          return imageDto;
        })
        .toList());
    return dto;
  }

  @Override
  @Transactional
  public Product addProduct(AddProductRequest request) {
    // check if the category is found in the DB
    // if Yes, set is as new product's category
    // if No, create a new category and set it as new product's category
    // then save the new product to the DB
    Category category = categoryRepository.findByName(request.getCategory())
        .orElseGet(() -> {
          Category newCategory = new Category();
          newCategory.setName(request.getCategory());
          return categoryRepository.save(newCategory);
        });

    return productRepository.save(createProduct(request, category));
  }

  @Transactional
  private Product updateExistingProduct(Product existingProduct, ProductUpdateRequest request) {
    if (request.getName() != null) {
      existingProduct.setName(request.getName());
    }
    if (request.getBrand() != null) {
      existingProduct.setBrand(request.getBrand());
    }
    if (request.getPrice() != null) {
      existingProduct.setPrice(request.getPrice());
    }
    if (request.getInventory() != 0) {
      existingProduct.setInventory(request.getInventory());
    }
    if (request.getDescription() != null) {
      existingProduct.setDescription(request.getDescription());
    }

    if (request.getCategory() != null && request.getCategory().getName() != null) {
      Category category = categoryRepository.findByName(request.getCategory().getName())
          .orElseGet(() -> {
            Category newCategory = new Category();
            newCategory.setName(request.getCategory().getName());
            return categoryRepository.save(newCategory);
          });
      existingProduct.setCategory(category);
    }

    return existingProduct;
  }

  @Override
  @Transactional
  public Product updateProduct(Long id, ProductUpdateRequest request) {
    return productRepository.findById(id)
        .map(existingProduct -> updateExistingProduct(existingProduct, request))
        .map(productRepository::save)
        .orElseThrow(() -> new ProductNotFoundException("Product not found with id: " + id));
  }

  @Override
  @Transactional
  public void deleteProduct(Long id) {
    productRepository.findById(id).ifPresentOrElse(productRepository::delete,
        () -> {
          throw new ProductNotFoundException("Product not found with id: " + id);
        });
  }

  @Override
  public Product getProductById(Long id) {
    return productRepository.findById(id)
        .orElseThrow(() -> new ProductNotFoundException("Product not found with id: " + id));
  }

  @Override
  public List<ProductDto> getAllProducts() {
    return productRepository.findAll()
        .stream()
        .map(this::convertToDto)
        .collect(Collectors.toList());
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
  public List<Product> getProductsByCategoryAndBrand(String category, String brand) {
    return productRepository.findByCategoryNameAndBrand(category, brand);
  }

  @Override
  public List<Product> getProductsByName(String name) {
    return productRepository.findByName(name);
  }

  @Override
  public List<Product> getProductsByBrandAndName(String brand, String name) {
    return productRepository.findByBrandAndName(brand, name);
  }

  @Override
  public Long countProductsByBrandAndName(String brand, String name) {
    return productRepository.countByBrandAndName(brand, name);
  }

}
