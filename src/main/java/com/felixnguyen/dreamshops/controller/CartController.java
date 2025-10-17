package com.felixnguyen.dreamshops.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.felixnguyen.dreamshops.dto.CartDto;
import com.felixnguyen.dreamshops.exceptions.ResourceNotFoundException;
import com.felixnguyen.dreamshops.response.ApiResponse;
import com.felixnguyen.dreamshops.service.cart.ICartService;

import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;

@Tag(name = "Cart APIs", description = "APIs for managing carts")
@RequiredArgsConstructor
@RestController
@RequestMapping("${api.prefix}/carts")
public class CartController {
  private final ICartService cartService;

  @GetMapping("/{cartId}")
  public ResponseEntity<ApiResponse> getCarts(@PathVariable Long cartId) {
    try {
      CartDto cart = cartService.getCartById(cartId);
      return ResponseEntity.ok().body(new ApiResponse("Get cart success", cart));
    } catch (ResourceNotFoundException e) {
      return ResponseEntity.badRequest().body(new ApiResponse(e.getMessage(), null));
    }
  }

  @DeleteMapping("/{cartId}")
  public ResponseEntity<ApiResponse> clearCart(@PathVariable Long cartId) {
    try {
      cartService.clearCart(cartId);
      return ResponseEntity.ok().body(new ApiResponse("Get cart success", null));
    } catch (ResourceNotFoundException e) {
      return ResponseEntity.badRequest().body(new ApiResponse(e.getMessage(), null));
    }
  }

  @GetMapping("/{cartId}/total-price")
  public ResponseEntity<ApiResponse> getTotalAmount(@PathVariable Long cartId) {
    try {
      CartDto cart = cartService.getCartById(cartId);
      return ResponseEntity.ok().body(new ApiResponse("Get total amount success", cart.getTotalAmount()));
    } catch (ResourceNotFoundException e) {
      return ResponseEntity.badRequest().body(new ApiResponse(e.getMessage(), null));
    }
  }

}
