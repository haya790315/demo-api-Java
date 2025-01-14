package com.dailycodework.dreamshops.model;

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

@Setter
@Getter
@AllArgsConstructor
@NoArgsConstructor
@Entity
public class Cart {
  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long id;
  private BigDecimal totalAmount = BigDecimal.ZERO;

  @OneToMany(mappedBy = "cart", cascade = CascadeType.ALL, orphanRemoval = true)
  private Set<CartItem> items = new HashSet<>();

  public void addItem(CartItem item) {
    this.items.add(item);
    item.setCart(null);
    updateTotalAmount();
  }

  public void removeItem(CartItem item) {
    this.items.remove(item);
    item.setCart(null);
    updateTotalAmount();
  }

  private void updateTotalAmount() {
    this.totalAmount = items.stream().map(item -> {
      BigDecimal unitPrice = item.getUnitPrice();
      if (unitPrice == null) {
        return BigDecimal.ZERO;
      }
      return unitPrice.multiply(new BigDecimal(item.getQuantity()));
    }).reduce(BigDecimal.ZERO, BigDecimal::add);
  }
}
