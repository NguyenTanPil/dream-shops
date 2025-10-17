package com.felixnguyen.dreamshops.model;

import java.math.BigDecimal;
import java.util.HashSet;
import java.util.Set;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.OneToMany;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Setter
@Getter
@NoArgsConstructor
@AllArgsConstructor
public class Cart {
  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long id;

  private BigDecimal totalAmount = BigDecimal.ZERO;

  @OneToMany(mappedBy = "cart", cascade = CascadeType.ALL, orphanRemoval = true)
  private Set<CartItem> cartItems = new HashSet<>();

  public void addItem(CartItem item) {
    this.cartItems.add(item);
    item.setCart(this);
    updateTotalAmount();
  }

  public void removeItem(CartItem item) {
    this.cartItems.remove(item);
    item.setCart(null);
    updateTotalAmount();
  }

  private void updateTotalAmount() {
    this.totalAmount = cartItems.stream()
        .map(item -> {
          BigDecimal unitPrict = item.getUnitPrice();
          if (unitPrict == null) {
            unitPrict = BigDecimal.ZERO;
          }
          return unitPrict.multiply(BigDecimal.valueOf(item.getQuantity()));
        }).reduce(BigDecimal.ZERO, BigDecimal::add);
  }
}
