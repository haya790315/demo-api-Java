package com.dailycodework.dreamshops.controller;

import java.math.BigDecimal;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.dailycodework.dreamshops.exceptions.ResourceNotFoundException;
import com.dailycodework.dreamshops.model.Cart;
import com.dailycodework.dreamshops.response.ApiResponse;
import com.dailycodework.dreamshops.service.cart.ICartService;

import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@RestController
@RequestMapping("${api.prefix}/carts")
public class CartController {
  private final ICartService cartService;

  @GetMapping("/{cartId}/my-cart")
  public ResponseEntity<ApiResponse> getCart(@PathVariable Long cartId) {
    try {
      Cart cart = cartService.getCart(cartId);
      return ResponseEntity.ok(new ApiResponse("Success", cart));
    } catch (ResourceNotFoundException e) {
      return ResponseEntity.status(HttpStatus.NOT_FOUND).body(new ApiResponse(e.getMessage(), null));
    }
  }

  @DeleteMapping("/{cartId}/clear")
  public ResponseEntity<ApiResponse> clearCart(@PathVariable Long cartId) {
    try {
      cartService.clearCart(cartId);
      return ResponseEntity.ok(new ApiResponse("Clear Cart!🥳", null));
    } catch (ResourceNotFoundException e) {
      return ResponseEntity.status(HttpStatus.NOT_FOUND).body(new ApiResponse(e.getMessage(), null));
    }
  }

  @GetMapping("/{cartId}/cart/total-price")
  public ResponseEntity<ApiResponse> getTotalAmount(@PathVariable Long cartId) {
    try {
      BigDecimal totalAmount = cartService.getTotalPrice(cartId);
      return ResponseEntity.ok(new ApiResponse("The Total Price", totalAmount));
    } catch (ResourceNotFoundException e) {
      return ResponseEntity.status(HttpStatus.NOT_FOUND).body(new ApiResponse(e.getMessage(), null));
    }
  }
}
