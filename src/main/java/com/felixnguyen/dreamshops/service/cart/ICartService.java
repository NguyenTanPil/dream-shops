package com.felixnguyen.dreamshops.service.cart;

import java.math.BigDecimal;

import com.felixnguyen.dreamshops.dto.CartDto;
import com.felixnguyen.dreamshops.model.Cart;

public interface ICartService {
  Cart getOriginCartById(Long id);

  CartDto getCartById(Long id);

  void clearCart(Long id);

  BigDecimal getTotalPrice(Long id);

  Long initializeNewCart();
}
