package com.example.food_ordering.service;

import com.example.food_ordering.dto.OrderDto;
import com.example.food_ordering.response.OrderResponseDto;

import java.util.List;

public interface OrderService {
    OrderDto createOrder(OrderDto orderDto);
    OrderDto getOrderById(Long orderId);
    OrderDto updateOrderStatus(Long orderId, String status);
    List<OrderResponseDto> getUserOrders();
    OrderResponseDto getOrderByOrderId(Long orderId);
}
