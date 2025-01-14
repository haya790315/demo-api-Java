package com.dailycodework.dreamshops.model;

import java.math.BigDecimal;

import com.fasterxml.jackson.annotation.JsonIgnore;

import jakarta.persistence.CascadeType;
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
  private BigDecimal unitPrice;
  private BigDecimal totalPrice;

  @JsonIgnore
  @ManyToOne
  @JoinColumn(name = "product_id")
  private Product product;

  @JsonIgnore
  @ManyToOne(cascade = CascadeType.ALL)
  @JoinColumn(name = "cart_id")
  private Cart cart;

  public void setQuantity(int quantity) {
    this.quantity = quantity;
    updateTotalPrice();
  }

  public void setUnitPrice(BigDecimal unitPrice) {
    this.unitPrice = unitPrice;
    updateTotalPrice();
  }

  private void updateTotalPrice() {
    this.totalPrice = this.unitPrice.multiply(new BigDecimal(this.quantity));
  }
}
