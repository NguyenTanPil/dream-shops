package com.felixnguyen.dreamshops.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.felixnguyen.dreamshops.model.Cart;

public interface CartRepository extends JpaRepository<Cart, Long> {

}
