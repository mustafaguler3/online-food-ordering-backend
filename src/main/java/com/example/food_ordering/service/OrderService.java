package com.example.food_ordering.service;

import com.example.food_ordering.dto.OrderDto;

public interface OrderService {
    OrderDto createOrder(OrderDto orderDto);
    OrderDto getOrderById(Long orderId);
    OrderDto updateOrderStatus(Long orderId, String status);
}
