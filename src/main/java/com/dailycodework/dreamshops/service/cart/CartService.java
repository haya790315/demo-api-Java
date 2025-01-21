package com.dailycodework.dreamshops.service.cart;

import java.math.BigDecimal;
import java.util.concurrent.atomic.AtomicLong;

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
  private final AtomicLong cartIdGenerator = new AtomicLong(0);

  @Override
  public Cart getCart(Long id) {
    Cart cart = cartRepository.findById(id).orElseThrow(() -> new ResourceNotFoundException("Cart not found"));
    return cart;
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
    Cart cart = getCart(id);
    return cart.getTotalAmount();
  }
  
  @Override
  public Long initializeNewCart() {
    Cart newCart = new Cart();
    Long newCartId = cartIdGenerator.incrementAndGet();
    newCart.setId(newCartId);
    return cartRepository.save(newCart).getId();
  }

  public Cart getCartByUserId(Long userId) {
    return cartRepository.findCartByUserId(userId).orElseThrow(() -> new ResourceNotFoundException("Cart not found"));
  }

}
