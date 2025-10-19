package com.felixnguyen.dreamshops.dto;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.Set;

import com.felixnguyen.dreamshops.enums.OrderStatus;

import lombok.Data;

@Data
public class OrderDto {
  private Long orderId;

  private LocalDate orderDate;

  private BigDecimal totalAmount;

  private OrderStatus status;

  private Set<OrderItemDto> orderItems;
}
