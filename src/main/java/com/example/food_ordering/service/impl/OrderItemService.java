package com.example.food_ordering.service.impl;

import com.example.food_ordering.dto.OrderItemDto;
import com.example.food_ordering.entities.Order;
import com.example.food_ordering.entities.OrderItem;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class OrderItemService {
    public List<OrderItem> createOrderItems(Order order, List<OrderItemDto> items) {
        if (items == null || items.isEmpty()) {
            throw new IllegalArgumentException("Order items cannot be null or empty");
        }

        return items.stream().map(item -> {
            OrderItem orderItem = new OrderItem();
            orderItem.setOrder(order);
            //orderItem.setProduct(item.getProduct());
            orderItem.setQuantity(item.getQuantity());
            orderItem.setUnitPrice(item.getUnitPrice());
            orderItem.setTotalPrice(item.getTotalPrice());
            return orderItem;
        }).collect(Collectors.toList());
    }
}
