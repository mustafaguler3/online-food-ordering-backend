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
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "product_id")
    private Product product;
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "basket_id")
    private Basket basket;

    private int quantity;      // Ürün miktarı
    private double unitPrice;  // Ürünün birim fiyatı
    private double discount;   // Ürün bazında indirim
    private double totalPrice; // Ürün toplam fiyatı (hesaplanır) // subTotal = unitPrice * quantity

    public void calculateTotalPrice(){
        this.totalPrice = (unitPrice * quantity) - discount;
    }

    public double calculateDiscountPercentage() {
        return (discount / (unitPrice * quantity)) * 100;
    }

}


















