package com.felixnguyen.dreamshops.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.felixnguyen.dreamshops.model.CartItem;

public interface CartItemRepository extends JpaRepository<CartItem, Long> {
  void deleteAllByCartId(Long id);
}
