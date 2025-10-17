package com.felixnguyen.dreamshops.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.felixnguyen.dreamshops.exceptions.ResourceNotFoundException;
import com.felixnguyen.dreamshops.response.ApiResponse;
import com.felixnguyen.dreamshops.service.cart.ICartItemService;
import com.felixnguyen.dreamshops.service.cart.ICartService;

import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;

@Tag(name = "Cart Item APIs", description = "APIs for managing cart items")
@RequiredArgsConstructor
@RestController
@RequestMapping("${api.prefix}/cartItems")
public class CartItemController {
  private final ICartItemService cartItemService;
  private final ICartService cartService;

  @PostMapping("/add")
  public ResponseEntity<ApiResponse> addItemToCart(@RequestParam(required = false) Long cartId,
      @RequestParam Long productId,
      @RequestParam Integer quantity) {
    try {
      if (cartId == null) {
        cartId = cartService.initializeNewCart();
      }
      cartItemService.addItemToCart(cartId, productId, quantity);
      return ResponseEntity.ok().body(new ApiResponse("Add item to cart success", null));
    } catch (ResourceNotFoundException e) {
      return ResponseEntity.badRequest().body(new ApiResponse(e.getMessage(), null));
    }
  }

  @DeleteMapping("/remove/{cartId}/{productId}")
  public ResponseEntity<ApiResponse> removeItemToCart(@PathVariable Long cartId,
      @PathVariable Long productId) {
    try {
      cartItemService.removeItemFromCart(cartId, productId);
      return ResponseEntity.ok().body(new ApiResponse("Remove item to cart success", null));
    } catch (ResourceNotFoundException e) {
      return ResponseEntity.badRequest().body(new ApiResponse(e.getMessage(), null));
    }
  }

  @PutMapping("/update-quantity/{cartId}/{productId}")
  public ResponseEntity<ApiResponse> updateItemQuantity(@PathVariable Long cartId,
      @PathVariable Long productId,
      @RequestParam Integer quantity) {
    try {
      cartItemService.updateItemQuantity(cartId, productId, quantity);
      return ResponseEntity.ok(new ApiResponse("Update Item Success", null));
    } catch (ResourceNotFoundException e) {
      return ResponseEntity.status(404).body(new ApiResponse(e.getMessage(), null));
    }

  }
}
