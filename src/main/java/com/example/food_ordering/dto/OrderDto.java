package com.example.food_ordering.dto;

import com.example.food_ordering.entities.*;
import com.example.food_ordering.enums.OrderStatus;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.util.ArrayList;
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
    private List<OrderItemDto> items;
    private long userId;
    private PaymentDto payment;
    private long basketId;
    private long addressId;
    private AddressDto shippingAddress;
}
