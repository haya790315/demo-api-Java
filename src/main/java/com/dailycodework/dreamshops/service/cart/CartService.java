package com.dailycodework.dreamshops.service.cart;

import java.math.BigDecimal;

import org.springframework.stereotype.Service;

import com.dailycodework.dreamshops.exceptions.ResourceNotFoundException;
import com.dailycodework.dreamshops.model.Cart;
import com.dailycodework.dreamshops.repository.CartItemRepository;
import com.dailycodework.dreamshops.repository.CartRepository;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class CartService implements ICartService {
  private final CartRepository cartRepository;
  private final CartItemRepository cartItemRepository;

  @Override
  public Cart getCart(Long id) {
    Cart cart = cartRepository.findById(id).orElseThrow(() -> new ResourceNotFoundException("Cart not found"));
    return cart;
  }

  @Override
  public void clearCart(Long id) {
    Cart cart = getCart(id);
    cartItemRepository.deleteAllByCartId(id);
    cart.getItems().clear();
  }

  @Override
  public BigDecimal getTotalPrice(Long id) {
    Cart cart = getCart(id);
    return cart.getTotalAmount();
  }

}
