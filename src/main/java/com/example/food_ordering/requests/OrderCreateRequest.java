package com.example.food_ordering.requests;

import com.example.food_ordering.entities.Address;
import com.example.food_ordering.entities.OrderItem;
import com.example.food_ordering.entities.Payment;
import com.example.food_ordering.enums.OrderStatus;
import lombok.Data;

import java.util.Date;
import java.util.List;

@Data
public class OrderCreateRequest {
    private Double totalAmount;
    private OrderStatus status;
    private Date orderDate;
    private List<OrderItem> items;
    private long userId;
    private Payment payment;
    private long basketId;
    private Address shippingAddress;
    private Address billingAddress;
}
