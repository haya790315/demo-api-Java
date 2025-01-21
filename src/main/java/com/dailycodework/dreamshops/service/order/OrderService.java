package com.dailycodework.dreamshops.service.order;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.HashSet;
import java.util.List;

import org.springframework.stereotype.Service;

import com.dailycodework.dreamshops.enums.OrderStatus;
import com.dailycodework.dreamshops.exceptions.ResourceNotFoundException;
import com.dailycodework.dreamshops.model.Cart;
import com.dailycodework.dreamshops.model.Order;
import com.dailycodework.dreamshops.model.OrderItem;
import com.dailycodework.dreamshops.model.Product;
import com.dailycodework.dreamshops.repository.OrderRepository;
import com.dailycodework.dreamshops.repository.ProductRepository;
import com.dailycodework.dreamshops.service.cart.CartService;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class OrderService implements IOrderService {
  private final OrderRepository orderRepository;
  private final ProductRepository productRepository;
  private final CartService cartService;

  @Override
  public Order getOrder(Long orderId) {
    return orderRepository.findById(orderId).orElseThrow(() -> new ResourceNotFoundException("Order not found"));
  }

  @Override
  @Transactional
  public Order placeOrder(Long userId) {
    Cart cart = cartService.getCartByUserId(userId);
    Order order = createOrder(cart);
    List<OrderItem> orderItems = createOrderItems(order, cart);
    order.setOrderItems(new HashSet<>(orderItems));
    order.setTotalAmount(calculateTotalAmount(order));
    Order savedOrder = orderRepository.save(order);
    cartService.clearCart(cart.getId());
    return savedOrder;
  }

  @Override
  public List<Order> getUserOrders(Long userId) {
    return orderRepository.findByUserId(userId);
  }

  private Order createOrder(Cart cart) {
    Order order = new Order();
    order.setUser(cart.getUser());
    order.getOrderItems().addAll(createOrderItems(order, cart));
    order.setTotalAmount(calculateTotalAmount(order));
    order.setOrderDate(LocalDate.now());
    order.setOrderStatus(OrderStatus.PENDING);
    return orderRepository.save(order);
  }

  private List<OrderItem> createOrderItems(Order order, Cart cart) {
    return cart.getItems().stream()
        .map(cartItem -> {
          Product product = cartItem.getProduct();
          product.setInventory(product.getInventory() - cartItem.getQuantity());
          productRepository.save(product);
          return new OrderItem(order, product, cartItem.getQuantity());
        }).toList();
  }

  private BigDecimal calculateTotalAmount(Order order) {
    return order.getOrderItems().stream()
        .map(orderItem -> orderItem.getProduct().getPrice().multiply(BigDecimal.valueOf(orderItem.getQuantity())))
        .reduce(BigDecimal.ZERO, BigDecimal::add);
  }
}
