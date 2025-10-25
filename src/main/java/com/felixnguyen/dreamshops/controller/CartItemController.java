package com.felixnguyen.dreamshops.controller;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.felixnguyen.dreamshops.exceptions.ResourceNotFoundException;
import com.felixnguyen.dreamshops.model.Cart;
import com.felixnguyen.dreamshops.model.User;
import com.felixnguyen.dreamshops.response.ApiResponse;
import com.felixnguyen.dreamshops.service.cart.ICartItemService;
import com.felixnguyen.dreamshops.service.cart.ICartService;
import com.felixnguyen.dreamshops.service.user.IUserService;

import io.jsonwebtoken.JwtException;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;

@Tag(name = "Cart Item APIs", description = "APIs for managing cart items")
@RequiredArgsConstructor
@RestController
@RequestMapping("${api.prefix}/cartItems")
public class CartItemController {
  private final ICartItemService cartItemService;
  private final ICartService cartService;
  private final IUserService userService;

  @PostMapping("/add")
  public ResponseEntity<ApiResponse> addItemToCart(@RequestParam(required = false) Long cartId,
      @RequestParam Long productId,
      @RequestParam Integer quantity) {
    try {
      User user = userService.getAuthenticationUser();
      Cart cart = cartService.initializeNewCart(user);
      cartItemService.addItemToCart(cart.getId(), productId, quantity);
      return ResponseEntity.ok().body(new ApiResponse("Add item to cart success", null));
    } catch (ResourceNotFoundException e) {
      return ResponseEntity.badRequest().body(new ApiResponse(e.getMessage(), null));
    } catch (JwtException e) {
      return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(new ApiResponse("Internal server error", null));
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
      return ResponseEntity.status(HttpStatus.NOT_FOUND).body(new ApiResponse(e.getMessage(), null));
    }

  }
}
