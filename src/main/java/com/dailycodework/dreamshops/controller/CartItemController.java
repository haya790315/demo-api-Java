package com.dailycodework.dreamshops.controller;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.dailycodework.dreamshops.exceptions.ResourceNotFoundException;
import com.dailycodework.dreamshops.response.ApiResponse;
import com.dailycodework.dreamshops.service.cart.ICartItemService;
import com.dailycodework.dreamshops.service.cart.ICartService;

import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@RestController
@RequestMapping("${api.prefix}/cartItems")
public class CartItemController {
  private final ICartItemService cartItemService;
  private final ICartService cartService;
  @PostMapping("/item/add")
  public ResponseEntity<ApiResponse> addItemToCart(@RequestParam(required = false) Long cartId, @RequestParam Long productId,
      @RequestParam Integer quantity) {
    try {
      if (cartId == null) {
        cartId = cartService.initializeNewCart();
      }
      cartItemService.addItemToCart(cartId, productId, quantity);
      return ResponseEntity.ok(new ApiResponse("Item added to cart 🥳", cartService.getCart(cartId)));
    } catch (ResourceNotFoundException e) {
      return ResponseEntity.status(HttpStatus.NOT_FOUND).body(new ApiResponse(e.getMessage(), null));
    }
  }

  @DeleteMapping("/cart/{cartId}/item/{productId}/remove")
  public ResponseEntity<ApiResponse> removeItemFromCar(@PathVariable Long cartId, @PathVariable Long productId) {
    try {
      cartItemService.removeItemFromCart(cartId, productId);
      return ResponseEntity.ok(new ApiResponse("Item removed from cart", null));
    } catch (ResourceNotFoundException e) {
      return ResponseEntity.status(HttpStatus.NOT_FOUND).body(new ApiResponse(e.getMessage(), null));
    }
  }

  @PostMapping("/cart/{cartId}/product/{productId}/update")
      public ResponseEntity<ApiResponse> updateItemQuantity(@PathVariable Long cartId, @PathVariable Long productId,
      @RequestParam Integer quantity) {
    try {
      cartItemService.updateItemQuantity(cartId, productId, quantity);
      return ResponseEntity.ok(new ApiResponse("Cart updated 🥳", cartService.getCart(cartId)));
    } catch (ResourceNotFoundException e) {
      return ResponseEntity.status(HttpStatus.NOT_FOUND).body(new ApiResponse(e.getMessage(), null));
    }

  }

}
