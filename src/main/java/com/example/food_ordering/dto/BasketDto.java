package com.example.food_ordering.dto;

import com.example.food_ordering.entities.BasketItem;
import com.example.food_ordering.entities.DiscountCode;
import lombok.Data;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Getter
@Setter
public class BasketDto {
    private Integer id;
    private List<BasketItemDto> items = new ArrayList<>();
    private double totalPrice;
    private double discount;
    private double grandTotal;
    private double tax;
    private DiscountCode discountCode;
    private UserDto user;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
    private String status;  // 'active', 'inactive'

    public BasketDto() {
    }
    public BasketDto(Integer id, List<BasketItemDto> items, double totalPrice, UserDto user) {
        this.id = id;
        this.items = items;
        this.totalPrice = totalPrice;
        this.user = user;
        this.createdAt = LocalDateTime.now();
        this.updatedAt = LocalDateTime.now();
        this.status = "active";  // default status
    }

    public void calculateTotals(){
        applyTaxAndDiscount();
        this.totalPrice = items.stream().mapToDouble(BasketItemDto::getTotalPrice).sum();
        this.grandTotal = (this.totalPrice - this.discount) + this.tax;
    }

    public double calculateBasketDiscountPercentage() {
        return (this.discount / this.totalPrice) * 100;
    }

    public void applyTaxAndDiscount() {
        this.tax = 5.0;
        this.discount = 10.0;
    }
}
