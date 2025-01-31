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
    @OneToMany(mappedBy = "basket",fetch = FetchType.EAGER,cascade = CascadeType.ALL)
    private List<BasketItem> basketItems = new ArrayList<>();
    @OneToOne(cascade = CascadeType.ALL,fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id")
    private User user;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "discount_code_id")
    private DiscountCode discountCode;

    private double totalPrice; // Tüm öğelerin toplam fiyatı
    private double discount;   // Toplam indirim
    private double tax;        // Vergi miktarı
    private double grandTotal; // İndirim ve vergi sonrası toplam fiyat
    private String status;     // Sepet durumu (ACTIVE, ORDERED, CANCELLED)
    private String currency;   // Para birimi (ör. USD, EUR)
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;

    @OneToMany(mappedBy = "basket")
    private List<Order> orders;

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
        if (discountCode != null && discountCode.isValid()) {
            applyDiscountCode();
        } else {
            this.discount = 0.0;
        }
    }

    public void applyDiscountCode() {
        if (discountCode != null) {
            this.discount = discountCode.getDiscountValue();
        }
    }

    @PrePersist
    public void prePersist() {
        this.createdAt = LocalDateTime.now();
    }

    @PreUpdate
    public void preUpdate() {
        this.updatedAt = LocalDateTime.now();
    }
}

















