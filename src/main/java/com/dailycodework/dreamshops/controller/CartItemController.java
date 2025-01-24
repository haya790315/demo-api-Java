package com.dailycodework.dreamshops.controller;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.dailycodework.dreamshops.exceptions.LowStockException;
import com.dailycodework.dreamshops.exceptions.ResourceNotFoundException;
import com.dailycodework.dreamshops.model.Cart;
import com.dailycodework.dreamshops.response.ApiResponse;
import com.dailycodework.dreamshops.service.cart.ICartItemService;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@RestController
@RequestMapping("${api.prefix}/cartItems")
public class CartItemController {
  private final ICartItemService cartItemService;

  @PostMapping("/item/add")
  public ResponseEntity<ApiResponse> addItemToCart(@RequestParam Long userId, @RequestParam Long productId,
          @RequestParam Integer quantity) {
    try {
      Cart cart = cartItemService.addItemToCart(userId, productId, quantity);
      return ResponseEntity.ok(new ApiResponse("Item added to cart 🥳", cart));
    } catch (ResourceNotFoundException e) {
      return ResponseEntity.status(HttpStatus.NOT_FOUND).body(new ApiResponse(e.getMessage(), null));
    } catch (LowStockException e) {
      return ResponseEntity.status(HttpStatus.CONFLICT).body(new ApiResponse(e.getMessage(), null));
    }
  }

  @DeleteMapping("/cart/{userId}/product/{productId}/remove")
  public ResponseEntity<ApiResponse> removeItemFromCar(@PathVariable Long userId, @PathVariable Long productId) {
    try {
      Cart cart = cartItemService.removeItemFromCart(userId, productId);
      return ResponseEntity.ok(new ApiResponse("Item removed from cart", cart));
    } catch (ResourceNotFoundException e) {
      return ResponseEntity.status(HttpStatus.NOT_FOUND).body(new ApiResponse(e.getMessage(), null));
    }
  }

  @PutMapping("/cart/{userId}/product/{productId}/update")
      public ResponseEntity<ApiResponse> updateItemQuantity(@PathVariable Long userId, @PathVariable Long productId,
      @RequestParam Integer quantity) {
    try {
      Cart cart = cartItemService.updateItemQuantity(userId, productId, quantity);
      return ResponseEntity.ok(new ApiResponse("Cart updated 🥳", cart));
    } catch (ResourceNotFoundException e) {
      return ResponseEntity.status(HttpStatus.NOT_FOUND).body(new ApiResponse(e.getMessage(), null));
    }

  }

}
