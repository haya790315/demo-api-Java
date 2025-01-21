package com.dailycodework.dreamshops.service.order;

import java.util.List;

import com.dailycodework.dreamshops.model.Order;

public interface IOrderService {

  Order getOrder(Long orderId);

  Order placeOrder(Long userId);

  List<Order> getUserOrders(Long userId);

}
