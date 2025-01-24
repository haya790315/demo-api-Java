package com.dailycodework.dreamshops.service.cart;

import com.dailycodework.dreamshops.model.Cart;
import com.dailycodework.dreamshops.model.CartItem;

public interface ICartItemService {
  Cart addItemToCart(Long userId, Long productId, int quantity);

  Cart removeItemFromCart(Long cartId, Long productId);

  Cart updateItemQuantity(Long cartId, Long productId, int quantity);

  CartItem getCartItem(Long cartId, Long productId);
}
