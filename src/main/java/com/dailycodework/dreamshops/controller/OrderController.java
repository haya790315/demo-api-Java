package com.dailycodework.dreamshops.controller;

import java.util.List;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.dailycodework.dreamshops.exceptions.ResourceNotFoundException;
import com.dailycodework.dreamshops.model.Order;
import com.dailycodework.dreamshops.response.ApiResponse;
import com.dailycodework.dreamshops.service.order.IOrderService;

import lombok.RequiredArgsConstructor;

import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

@RequiredArgsConstructor
@RestController
@RequestMapping("${api.prefix}/orders")

public class OrderController {
  private final IOrderService orderService;

  @GetMapping("/{orderId}/order")
  public ResponseEntity<ApiResponse> getOrderById(@PathVariable Long orderId) {
    try {
      Order order = orderService.getOrder(orderId);
      return ResponseEntity.ok(new ApiResponse("Orders Found 🥳", order));
    } catch (ResourceNotFoundException e) {
      return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
          .body(new ApiResponse("Order Not Found😵‍💫", null));
    }

  }

  @GetMapping("/user/{userId}/orders")
  public ResponseEntity<ApiResponse> getOrdersByUser(@PathVariable Long userId) {
    try {
      List<Order> orders = orderService.getUserOrders(userId);
      return ResponseEntity.ok(new ApiResponse("Have these Orders 🥳", orders));
    } catch (ResourceNotFoundException e) {
      return ResponseEntity.status(HttpStatus.NOT_FOUND)
          .body(new ApiResponse("No Order Found😵‍💫", null));
    }

  }

  @PostMapping("/order")
  public ResponseEntity<ApiResponse> createOrder(@RequestParam Long userId) {
    try {
      Order order = orderService.placeOrder(userId);
      return ResponseEntity.ok(new ApiResponse("Order Created Success 🥳", order));
    } catch (Exception e) {
      return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(new ApiResponse(e.getMessage(), null));
    }
  }

}
