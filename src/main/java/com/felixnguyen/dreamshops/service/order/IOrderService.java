package com.felixnguyen.dreamshops.service.order;

import java.util.List;

import com.felixnguyen.dreamshops.dto.OrderDto;

public interface IOrderService {
  OrderDto placeOrder(Long userId);

  OrderDto getOrder(Long orderId);

  List<OrderDto> getUserOrders(Long orderId);
}
