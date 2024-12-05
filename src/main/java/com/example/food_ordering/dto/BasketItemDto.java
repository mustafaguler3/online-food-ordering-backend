package com.example.food_ordering.dto;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class BasketItemDto {

    private int id;
    private ProductDto product;
    private int quantity;
    private double totalPrice;
    private double price;

}





















