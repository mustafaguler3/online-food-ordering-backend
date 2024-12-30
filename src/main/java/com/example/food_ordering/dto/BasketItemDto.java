package com.example.food_ordering.dto;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class BasketItemDto {

    private Integer id;
    private Long productId;
    private String productName;
    private String productImage;
    private String description;
    private int quantity;
    private double discount;
    private double totalPrice;
    private long basketId;
    private double unitPrice;

}





















