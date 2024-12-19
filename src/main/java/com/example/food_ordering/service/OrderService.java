package com.example.food_ordering.service;

import com.example.food_ordering.dto.OrderDto;

public interface OrderService {
    void createOrder(OrderDto orderDto);
    OrderDto getOrderById(Long orderId);
    OrderDto updateOrderStatus(Long orderId, String status);
}
