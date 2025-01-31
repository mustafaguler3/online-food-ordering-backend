package com.example.food_ordering.entities;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;

@Entity
@Getter
@Setter
public class BasketItem {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;
    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "product_id")
    private Product product;
    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "basket_id")
    private Basket basket;

    private int quantity;
    private double unitPrice;
    private double discount;
    private double totalPrice;

    public void calculateTotalPrice(){
        this.totalPrice = (unitPrice * quantity) - discount;
    }

    public double calculateDiscountPercentage() {
        return (discount / (unitPrice * quantity)) * 100;
    }

}


















