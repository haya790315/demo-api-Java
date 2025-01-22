package com.dailycodework.dreamshops.model;

import java.math.BigDecimal;

import com.fasterxml.jackson.annotation.JsonIgnore;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Setter
@Getter
@AllArgsConstructor
@NoArgsConstructor
@Entity
public class CartItem {
  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long id;

  private int quantity;
  private BigDecimal totalPrice;

  @ManyToOne
  @JoinColumn(name = "product_id")
  // @JsonIgnore
  private Product product;

  @ManyToOne
  @JoinColumn(name = "cart_id")
  @JsonIgnore
  private Cart cart;

  public void setQuantity(int newQuantity) {
    quantity = newQuantity;
    updateTotalPrice();
  }

  private void updateTotalPrice() {
    totalPrice = product.getPrice().multiply(new BigDecimal(this.quantity));
  }
}
