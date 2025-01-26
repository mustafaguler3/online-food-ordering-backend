package com.example.food_ordering.dto;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class BasketItemDto {

    private int id;
    private int productId;
    private String productName;
    private String productImage;
    private String description;
    private int quantity;
    private double discount;
    private double totalPrice;
    private long basketId;
    private double unitPrice;

    public void calculateTotalPrice(){
        this.totalPrice = (unitPrice * quantity) - discount;
    }

    public double calculateDiscountPercentage() {
        return (discount / (unitPrice * quantity)) * 100;
    }
}





















