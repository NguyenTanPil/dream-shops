package com.felixnguyen.dreamshops.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.felixnguyen.dreamshops.model.Product;

public interface ProductRepository extends JpaRepository<Product, Long> {
  List<Product> findByCategoryName(String categoryName);

  List<Product> findByBrand(String brand);

  List<Product> findByName(String name);

  List<Product> findByCategoryNameAndBrand(String category, String brand);

  List<Product> findByBrandAndName(String brand, String name);

  Long countByBrandAndName(String brand, String name);
}
