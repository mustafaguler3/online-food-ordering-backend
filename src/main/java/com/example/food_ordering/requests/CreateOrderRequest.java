package com.example.food_ordering.requests;

import com.example.food_ordering.entities.Address;
import com.example.food_ordering.entities.OrderItem;
import com.example.food_ordering.entities.Payment;
import com.example.food_ordering.enums.OrderStatus;
import lombok.Data;

import java.util.Date;
import java.util.List;

@Data
public class CreateOrderRequest {
    private Double totalAmount;
    private Date orderDate;
    private long userId;
    private long basketId;
}
