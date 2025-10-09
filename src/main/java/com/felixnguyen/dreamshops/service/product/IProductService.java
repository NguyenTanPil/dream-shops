package com.felixnguyen.dreamshops.service.product;

import java.util.List;

import com.felixnguyen.dreamshops.dto.ProductDto;
import com.felixnguyen.dreamshops.model.Product;
import com.felixnguyen.dreamshops.request.AddProductRequest;
import com.felixnguyen.dreamshops.request.ProductUpdateRequest;

public interface IProductService {
  Product addProduct(AddProductRequest product);

  Product updateProduct(Long id, ProductUpdateRequest product);

  void deleteProduct(Long id);

  Product getProductById(Long id);

  List<ProductDto> getAllProducts();

  List<Product> getProductsByCategory(String category);

  List<Product> getProductsByBrand(String brand);

  List<Product> getProductsByCategoryAndBrand(String category, String brand);

  List<Product> getProductsByName(String name);

  List<Product> getProductsByBrandAndName(String brand, String name);

  Long countProductsByBrandAndName(String brand, String name);

}
