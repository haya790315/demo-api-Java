package com.dailycodework.dreamshops.dto;

import java.util.List;

import com.dailycodework.dreamshops.model.Cart;

import lombok.Data;

@Data
public class UserDto {
  private Long id;
  private String firstName;
  private String lastName;
  private List<OrderDto> orders;
  private Cart cart;
}
