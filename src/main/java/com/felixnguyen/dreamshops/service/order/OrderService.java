package com.felixnguyen.dreamshops.service.order;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.HashSet;
import java.util.List;

import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;

import com.felixnguyen.dreamshops.dto.OrderDto;
import com.felixnguyen.dreamshops.enums.OrderStatus;
import com.felixnguyen.dreamshops.exceptions.ResourceNotFoundException;
import com.felixnguyen.dreamshops.model.Cart;
import com.felixnguyen.dreamshops.model.Order;
import com.felixnguyen.dreamshops.model.OrderItem;
import com.felixnguyen.dreamshops.model.Product;
import com.felixnguyen.dreamshops.repository.OrderRepository;
import com.felixnguyen.dreamshops.repository.ProductRepository;
import com.felixnguyen.dreamshops.service.cart.ICartService;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class OrderService implements IOrderService {
  private final OrderRepository orderRepository;
  private final ProductRepository productRepository;
  private final ICartService cartService;
  private final ModelMapper modelMapper;

  @Override
  public OrderDto placeOrder(Long userId) {
    try {
      Cart cart = cartService.getOriginCartByUserId(userId);
      Order order = createOrder(cart);

      List<OrderItem> orderItems = createOrderItems(order, cart);

      BigDecimal totalAmount = calculateTotalAmount(orderItems);
      order.setTotalAmount(totalAmount);
      order.setOrderItems(new HashSet<>(orderItems));

      Order savedOrder = orderRepository.save(order);
      cartService.clearCart(cart.getId());
      return convertToDto(savedOrder);
    } catch (Exception e) {
      throw new RuntimeException("Cannot place order: " + e.getMessage(), e);
    }
  }

  @Override
  public OrderDto getOrder(Long orderId) {
    return orderRepository.findById(orderId).map(this::convertToDto)
        .orElseThrow(() -> new ResourceNotFoundException("Order not found with id: " + orderId));
  }

  @Override
  public List<OrderDto> getUserOrders(Long userId) {
    List<Order> orders = orderRepository.findByUserId(userId);
    return orders.stream().map(this::convertToDto).toList();
  }

  private OrderDto convertToDto(Order order) {
    return modelMapper.map(order, OrderDto.class);
  }

  private Order createOrder(Cart cart) {
    Order order = new Order();
    order.setUser(cart.getUser());
    order.setStatus(OrderStatus.PENDING);
    order.setOrderDate(LocalDate.now());
    return order;
  }

  private List<OrderItem> createOrderItems(Order order, Cart cart) {
    return cart.getCartItems()
        .stream()
        .map(cartItem -> {
          Product product = cartItem.getProduct();
          product.setInventory(product.getInventory() - cartItem.getQuantity());
          productRepository.save(product);
          return new OrderItem(cartItem.getQuantity(), cartItem.getUnitPrice(), order,
              product);
        }).toList();
  }

  private BigDecimal calculateTotalAmount(List<OrderItem> orderItems) {
    return orderItems
        .stream()
        .map(item -> item.getPrice().multiply(BigDecimal.valueOf(item.getQuantity())))
        .reduce(BigDecimal.ZERO, BigDecimal::add);
  }

}
