package com.felixnguyen.dreamshops.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.felixnguyen.dreamshops.model.OrderItem;

public interface OrderItemRepository extends JpaRepository<OrderItem, Long> {

}
