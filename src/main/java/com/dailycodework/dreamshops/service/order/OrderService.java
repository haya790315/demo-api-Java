package com.dailycodework.dreamshops.service.order;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;

import org.springframework.stereotype.Service;
import org.modelmapper.ModelMapper;

import com.dailycodework.dreamshops.dto.OrderDto;
import com.dailycodework.dreamshops.enums.OrderStatus;
import com.dailycodework.dreamshops.exceptions.LowStockException;
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
  // ModelMapper can be used to map data between different objects
  private final ModelMapper modelMapper;

  @Override
  public OrderDto getOrder(Long orderId) {
    return convertToDto(
        orderRepository.findById(orderId).orElseThrow(() -> new ResourceNotFoundException("Order not found")));
  }

  @Override
  @Transactional
  public Order placeOrder(Long userId) {
    Cart cart = cartService.getCartByUserId(userId);
    if (cart.getItems().isEmpty()) {
      return null;
    }
    Order order = createOrder(cart);
    order.setTotalAmount(calculateTotalAmount(order));
    Order savedOrder = orderRepository.save(order);
    cartService.clearCart(cart.getId());
    return savedOrder;
  }

  @Override
  public List<OrderDto> getUserOrders(Long userId) {
    return orderRepository.findByUserId(userId).stream().map(this::convertToDto).toList();
  }

  private Order createOrder(Cart cart) {
    Order order = new Order();
    order.setUser(cart.getUser());
    List<OrderItem> orderItems = createOrderItems(order, cart);
    order.getOrderItems().addAll(orderItems);
    order.setTotalAmount(calculateTotalAmount(order));
    order.setOrderDate(LocalDate.now());
    order.setOrderStatus(OrderStatus.PENDING);
    return orderRepository.save(order);
  }

  private List<OrderItem> createOrderItems(Order order, Cart cart) {
    return cart.getItems().stream()
        .map(cartItem -> {
          Product product = cartItem.getProduct();
          if (product.getInventory() < cartItem.getQuantity()) {
            throw new LowStockException(product.getName());
          }
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

  private OrderDto convertToDto(Order order) {
    return modelMapper.map(order, OrderDto.class);
  }
}
