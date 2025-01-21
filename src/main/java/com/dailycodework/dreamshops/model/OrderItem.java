package com.dailycodework.dreamshops.model;

import com.fasterxml.jackson.annotation.JsonIgnore;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@Entity
public class OrderItem {
  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long id;

  private int quantity;

  @ManyToOne(cascade = CascadeType.ALL)
  @JoinColumn(name = "the_order")
  @JsonIgnore
  private Order order;

  @ManyToOne
  @JoinColumn(name = "product_id")
  private Product product;

  public OrderItem(Order order, Product product, int quantity) {
    this.order = order;
    this.product = product;
    this.quantity = quantity;
  }
}
