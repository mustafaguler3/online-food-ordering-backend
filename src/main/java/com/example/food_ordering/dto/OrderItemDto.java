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
    private Long orderId;
    private OrderDto orderDto;
    private long productId;
    private String productName;
    private Integer quantity;
    private Double unitPrice;
    private Double totalPrice;

    private Double discountPercentage;
    private Double taxRate;


    public Double getTotalPrice() {
        double priceAfterDiscount = unitPrice - (unitPrice * (discountPercentage != null ? discountPercentage : 0) / 100);
        double priceAfterTax = priceAfterDiscount + (priceAfterDiscount * (taxRate != null ? taxRate : 0) / 100);
        return priceAfterTax * quantity;
    }

}
