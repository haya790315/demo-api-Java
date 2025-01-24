package com.dailycodework.dreamshops.service.cart;

import java.math.BigDecimal;

import org.springframework.stereotype.Service;

import com.dailycodework.dreamshops.exceptions.ResourceNotFoundException;
import com.dailycodework.dreamshops.model.Cart;
import com.dailycodework.dreamshops.repository.CartItemRepository;
import com.dailycodework.dreamshops.repository.CartRepository;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class CartService implements ICartService {
  private final CartRepository cartRepository;
  private final CartItemRepository cartItemRepository;

  @Override
  public Cart getCart(Long id) {
    return cartRepository.findById(id).orElseThrow(() -> new ResourceNotFoundException("Cart not found"));
  }

  @Override
  @Transactional
  public void clearCart(Long id) {
    Cart cart = getCart(id);
    cartItemRepository.deleteAllByCartId(id);
    cart.getItems().clear();
    cart.setTotalAmount(BigDecimal.ZERO);
    cartRepository.save(cart);
  }

  @Override
  public BigDecimal getTotalPrice(Long id) {
    return getCart(id).getTotalAmount();
  }

  @Override
  public Cart getCartByUserId(Long userId) {
    return cartRepository.findByUserId(userId).orElseThrow(() -> new ResourceNotFoundException("Cart not found"));
  }
}
