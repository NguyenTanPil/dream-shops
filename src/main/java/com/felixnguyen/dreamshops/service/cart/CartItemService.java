package com.felixnguyen.dreamshops.service.cart;

import java.math.BigDecimal;

import org.springframework.stereotype.Service;

import com.felixnguyen.dreamshops.exceptions.ResourceNotFoundException;
import com.felixnguyen.dreamshops.model.Cart;
import com.felixnguyen.dreamshops.model.CartItem;
import com.felixnguyen.dreamshops.model.Product;
import com.felixnguyen.dreamshops.repository.CartRepository;
import com.felixnguyen.dreamshops.service.product.ProductService;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class CartItemService implements ICartItemService {
  private final CartRepository cartRepository;
  private final ProductService productService;
  private final CartService cartService;

  @Override
  public void addItemToCart(Long cartId, Long productId, int quantity) {
    // 1. get cart
    // 2. get product
    // 3. check if the product already in the cart
    // 4. if yes, update quantity
    // 5. if no, create new cart item
    Cart cart = cartService.getOriginCartById(cartId);
    Product product = productService.getOriginProductById(productId);
    CartItem cartItem = cart.getCartItems()
        .stream()
        .filter(item -> item.getProduct().getId().equals(productId))
        .findFirst()
        .orElse(new CartItem());

    if (cartItem.getId() == null) {
      cartItem.setCart(cart);
      cartItem.setProduct(product);
      cartItem.setQuantity(quantity);
      cartItem.setUnitPrice(product.getPrice());
    } else {
      cartItem.setQuantity(cartItem.getQuantity() + quantity);
    }

    cartItem.setTotalPrice();
    cart.addItem(cartItem);
    cartRepository.save(cart);
  }

  @Override
  public void removeItemFromCart(Long cartId, Long productId) {
    Cart cart = cartService.getOriginCartById(cartId);
    CartItem cartItem = getCartItem(cartId, productId);
    cart.removeItem(cartItem);

    cartRepository.save(cart);
  }

  @Override
  public void updateItemQuantity(Long cartId, Long productId, int quantity) {
    Cart cart = cartService.getOriginCartById(cartId);
    cart.getCartItems().stream()
        .filter(item -> item.getProduct().getId().equals(productId))
        .findFirst()
        .ifPresent(item -> {
          item.setQuantity(quantity);
          item.setUnitPrice(item.getProduct().getPrice());
          item.setTotalPrice();
        });
    BigDecimal totalAmount = cart.getCartItems()
        .stream().map(CartItem::getTotalPrice)
        .reduce(BigDecimal.ZERO, BigDecimal::add);

    cart.setTotalAmount(totalAmount);
    cartRepository.save(cart);
  }

  private CartItem getCartItem(Long cartId, Long productId) {
    Cart cart = cartService.getOriginCartById(cartId);
    return cart.getCartItems().stream()
        .filter(item -> item.getProduct().getId().equals(productId))
        .findFirst()
        .orElseThrow(() -> new ResourceNotFoundException("Product not found in cart"));
  }
}
