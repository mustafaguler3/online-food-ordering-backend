package com.example.food_ordering.entities;

import com.example.food_ordering.dto.BasketItemDto;
import com.example.food_ordering.dto.UserDto;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Entity
@Getter
@Setter
public class Basket {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;
    @OneToMany(mappedBy = "basket",fetch = FetchType.LAZY,cascade = CascadeType.ALL)
    private Set<BasketItem> basketItems = new HashSet<>();
    @OneToOne(cascade = CascadeType.ALL,fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id")
    private User user;

    private double totalPrice; // Tüm öğelerin toplam fiyatı
    private double discount;   // Toplam indirim
    private double tax;        // Vergi miktarı
    private double grandTotal; // İndirim ve vergi sonrası toplam fiyat
    private String status;     // Sepet durumu (ACTIVE, ORDERED, CANCELLED)
    private String currency;   // Para birimi (ör. USD, EUR)
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;

    public Basket() {
    }

    public void calculateTotals(){
        applyTaxAndDiscount();
        this.totalPrice = basketItems.stream().mapToDouble(BasketItem::getTotalPrice).sum();
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

















