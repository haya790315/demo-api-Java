package com.dailycodework.dreamshops.model;

import java.math.BigDecimal;
import java.util.HashSet;
import java.util.Set;

import com.fasterxml.jackson.annotation.JsonManagedReference;

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
  @JsonManagedReference
  private Set<CartItem> items = new HashSet<>();

  public void addItem(CartItem item) {
    items.add(item);
    item.setCart(this);
    updateTotalAmount();
  }

  public void removeItem(CartItem item) {
    items.remove(item);
    updateTotalAmount();
  }

  public BigDecimal getTotalAmount() {
    updateTotalAmount();
    return totalAmount;
  }

  public void updateTotalAmount() {
    totalAmount = items.stream().map(item -> {
      return item.getTotalPrice();
    }).reduce(BigDecimal.ZERO, BigDecimal::add);
  }
}
