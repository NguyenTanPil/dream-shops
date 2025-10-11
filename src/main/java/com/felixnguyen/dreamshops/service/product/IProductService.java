package com.felixnguyen.dreamshops.service.product;

import java.util.List;

import com.felixnguyen.dreamshops.dto.ProductDto;
import com.felixnguyen.dreamshops.model.Product;
import com.felixnguyen.dreamshops.request.AddProductRequest;
import com.felixnguyen.dreamshops.request.ProductUpdateRequest;

public interface IProductService {
  ProductDto addProduct(AddProductRequest product);

  ProductDto updateProduct(Long id, ProductUpdateRequest product);

  void deleteProduct(Long id);

  ProductDto getProductById(Long id);

  Product getOriginProductById(Long id);

  List<ProductDto> getAllProducts();

  List<ProductDto> getProductsByCategory(String category);

  List<ProductDto> getProductsByBrand(String brand);

  List<ProductDto> getProductsByCategoryAndBrand(String category, String brand);

  List<ProductDto> getProductsByName(String name);

  List<ProductDto> getProductsByBrandAndName(String brand, String name);

  Long countProductsByBrandAndName(String brand, String name);

}
