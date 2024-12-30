package com.example.food_ordering.dto;

import com.example.food_ordering.entities.Order;
import com.example.food_ordering.entities.Product;
import jakarta.persistence.*;
import lombok.Data;
import lombok.Getter;
import lombok.Setter;


@Getter
@Setter
public class OrderItemDto {
    private Long id;
    private long orderId;
    private long productId;
    private Integer quantity;
    private Double unitPrice;
    private Double totalPrice;
}
