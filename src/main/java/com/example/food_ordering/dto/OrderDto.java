package com.example.food_ordering.dto;

import com.example.food_ordering.entities.*;
import com.example.food_ordering.enums.OrderStatus;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.util.Date;
import java.util.List;

@Getter
@Setter
public class OrderDto {
    private Long id;
    private String orderReferenceNumber;
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
