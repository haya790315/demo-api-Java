package com.dailycodework.dreamshops.service.cart;

import org.springframework.stereotype.Service;

import com.dailycodework.dreamshops.exceptions.ResourceNotFoundException;
import com.dailycodework.dreamshops.model.Cart;
import com.dailycodework.dreamshops.model.CartItem;
import com.dailycodework.dreamshops.model.Product;
import com.dailycodework.dreamshops.repository.CartItemRepository;
import com.dailycodework.dreamshops.repository.CartRepository;
import com.dailycodework.dreamshops.service.product.IProductService;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class CartItemService implements ICartItemService {
  private final CartItemRepository cartItemRepository;
  private final CartRepository cartRepository;
  private final IProductService productService;
  private final ICartService cartService;

  @Override
  public Cart addItemToCart(Long userId, Long productId, int quantity) {
    try {
      // 1.get Cart
      Cart cart = cartService.getCartByUserId(userId);
      // 2.get Product
      Product product = productService.getProductById(productId);
      // 3.check if the product already in the cart
      CartItem cartItem = cart.getItems().stream().filter(item -> item.getProduct().getId().equals(productId))
          .findFirst()
          .orElse(new CartItem());
      if (cartItem.getId() == null) {
        // 4.if No, then add the product to the cart with the requested quantity
        cartItem.setProduct(product);
        cartItem.setCart(cart);
      }

      cartItem.setQuantity(quantity + cartItem.getQuantity());

      cart.addItem(cartItem);
      cartItemRepository.save(cartItem);
      return cartRepository.save(cart);
    } catch (Exception e) {
      throw new RuntimeException("Failed to add item to cart", e);
    }

  }

  @Override
  public Cart removeItemFromCart(Long userId, Long productId) {
    Cart cart = cartRepository.findByUserId(userId)
        .orElseThrow(() -> new ResourceNotFoundException("Cart not found 😵‍💫"));
    cart.removeItem(getCartItem(userId, productId));
    return cartRepository.save(cart);
  }

  @Override
  public Cart updateItemQuantity(Long userId, Long productId, int quantity) {
    Cart cart = cartService.getCartByUserId(userId);
    cart.getItems().stream().filter(item -> item.getProduct().getId().equals(productId)).findFirst()
        .ifPresentOrElse(item -> {
          item.setQuantity(quantity);
          cartItemRepository.save(item);
        }, () -> {
          throw new ResourceNotFoundException("Item not found");
        });
    cart.updateTotalAmount();
    return cartRepository.save(cart);
  }

  @Override
  public CartItem getCartItem(Long userId, Long productId) {
    Cart cart = cartRepository.findByUserId(userId)
        .orElseThrow(() -> new ResourceNotFoundException("Cart not found 😵‍💫"));
    CartItem cartItem = cart.getItems().stream().filter(item -> item.getProduct().getId().equals(productId)).findFirst()
        .orElseThrow(() -> new ResourceNotFoundException("Item not found 😵‍💫"));
    return cartItem;
  }

}
