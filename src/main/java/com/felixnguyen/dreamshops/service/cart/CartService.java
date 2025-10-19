package com.felixnguyen.dreamshops.service.cart;

import java.math.BigDecimal;

import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.felixnguyen.dreamshops.dto.CartDto;
import com.felixnguyen.dreamshops.exceptions.ResourceNotFoundException;
import com.felixnguyen.dreamshops.model.Cart;
import com.felixnguyen.dreamshops.model.User;
import com.felixnguyen.dreamshops.repository.CartItemRepository;
import com.felixnguyen.dreamshops.repository.CartRepository;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class CartService implements ICartService {
  private final CartRepository cartRepository;
  private final CartItemRepository cartItemRepository;
  private final ModelMapper modelMapper;

  private CartDto convertToDto(Cart cart) {
    return modelMapper.map(cart, CartDto.class);
  }

  @Override
  public Cart getOriginCartById(Long id) {
    return cartRepository.findById(id)
        .orElseThrow(() -> new ResourceNotFoundException("Cart not found with id: " + id));
  }

  @Override
  public CartDto getCartById(Long id) {
    Cart cart = cartRepository.findById(id)
        .orElseThrow(() -> new ResourceNotFoundException("Cart not found with id: " + id));

    BigDecimal totalAmount = cart.getTotalAmount();
    cart.setTotalAmount(totalAmount);
    return convertToDto(cartRepository.save(cart));
  }

  @Override
  @Transactional
  public void clearCart(Long id) {
    Cart cart = cartRepository.findById(id).orElse(null);

    if (cart != null) {
      cartItemRepository.deleteAllByCartId(id);
      cart.getCartItems().clear();
      cartRepository.deleteById(id);
    } else {
      throw new ResourceNotFoundException("Not found cart id: " + id);
    }
  }

  @Override
  public BigDecimal getTotalPrice(Long id) {
    return cartRepository.findById(id)
        .map(cart -> cart.getTotalAmount())
        .orElseThrow(() -> new ResourceNotFoundException("Not found cart id: " + id));
  }

  public Cart initializeNewCart(User user) {
    return cartRepository.findByUserId(user.getId()).orElseGet(() -> {
      Cart cart = new Cart();
      cart.setUser(user);
      return cartRepository.save(cart);
    });
  }

  @Override
  public CartDto getCartByUserId(Long userId) {
    return cartRepository.findByUserId(userId).map(this::convertToDto)
        .orElseThrow(() -> new ResourceNotFoundException("Cart not found for user id: " + userId));
  }

  @Override
  public Cart getOriginCartByUserId(Long userId) {
    return cartRepository.findByUserId(userId)
        .orElseThrow(() -> new ResourceNotFoundException("Cart not found for user id: " + userId));
  }
}
