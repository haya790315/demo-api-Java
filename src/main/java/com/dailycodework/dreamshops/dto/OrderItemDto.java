package com.dailycodework.dreamshops.dto;

import java.math.BigDecimal;

import lombok.Data;

@Data
public class OrderItemDto {
  private Long id;

  private int quantity;

  private String productName;

  private BigDecimal productPrice;

}
