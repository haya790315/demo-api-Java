package com.dailycodework.dreamshops.service.order;

import java.util.List;

import com.dailycodework.dreamshops.dto.OrderDto;
import com.dailycodework.dreamshops.model.Order;

public interface IOrderService {

  OrderDto getOrder(Long orderId);

  Order placeOrder(Long userId);

  List<OrderDto> getUserOrders(Long userId);

}
