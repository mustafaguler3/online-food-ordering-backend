package com.example.food_ordering.service;

import com.example.food_ordering.dto.OrderDto;
import com.example.food_ordering.dto.PaymentDto;
import com.example.food_ordering.entities.Order;

import java.util.List;

public interface PaymentService {
    boolean processPayment(PaymentDto paymentDto, OrderDto order);
}
