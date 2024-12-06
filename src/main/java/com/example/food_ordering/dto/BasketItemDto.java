package com.example.food_ordering.dto;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class BasketItemDto {

    private int id;
    private Long productId;
    private int quantity;
    private double totalPrice;
    private long basketId;
    private double price;

}





















