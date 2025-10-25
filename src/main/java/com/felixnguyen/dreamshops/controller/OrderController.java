package com.felixnguyen.dreamshops.controller;

import java.util.List;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.felixnguyen.dreamshops.dto.OrderDto;
import com.felixnguyen.dreamshops.exceptions.ResourceNotFoundException;
import com.felixnguyen.dreamshops.response.ApiResponse;
import com.felixnguyen.dreamshops.service.order.IOrderService;

import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;

@Tag(name = "Order APIs", description = "APIs for managing orders")
@RequiredArgsConstructor
@RestController
@RequestMapping("${api.prefix}/orders")
public class OrderController {
  private final IOrderService orderService;

  @GetMapping("/{orderId}")
  public ResponseEntity<ApiResponse> getOrderById(@PathVariable Long orderId) {
    try {
      OrderDto order = orderService.getOrder(orderId);
      return ResponseEntity.ok().body(new ApiResponse("Get order success", order));
    } catch (ResourceNotFoundException e) {
      return ResponseEntity.status(HttpStatus.NOT_FOUND).body(new ApiResponse(e.getMessage(), null));
    }
  }

  @PostMapping("/{userId}")
  public ResponseEntity<ApiResponse> createOrder(@PathVariable Long userId) {
    try {
      OrderDto order = orderService.placeOrder(userId);
      return ResponseEntity.ok().body(new ApiResponse("Create order success", order));
    } catch (ResourceNotFoundException e) {
      return ResponseEntity.badRequest().body(new ApiResponse(e.getMessage(), null));
    }
  }

  @GetMapping("/{userId}/orders")
  public ResponseEntity<ApiResponse> getUserOrders(@PathVariable Long userId) {
    try {
      List<OrderDto> orders = orderService.getUserOrders(userId);
      return ResponseEntity.ok().body(new ApiResponse("Get orders success", orders));
    } catch (ResourceNotFoundException e) {
      return ResponseEntity.status(HttpStatus.NOT_FOUND).body(new ApiResponse(e.getMessage(), null));
    }
  }

}
