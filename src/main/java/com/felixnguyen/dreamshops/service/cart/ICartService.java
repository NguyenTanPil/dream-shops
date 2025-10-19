package com.felixnguyen.dreamshops.service.cart;

import java.math.BigDecimal;

import com.felixnguyen.dreamshops.dto.CartDto;
import com.felixnguyen.dreamshops.model.Cart;
import com.felixnguyen.dreamshops.model.User;

public interface ICartService {
  Cart getOriginCartById(Long id);

  CartDto getCartById(Long id);

  CartDto getCartByUserId(Long id);

  Cart getOriginCartByUserId(Long id);

  void clearCart(Long id);

  BigDecimal getTotalPrice(Long id);

  Cart initializeNewCart(User user);
}
