package com.example.food_ordering.entities;

import com.example.food_ordering.enums.DiscountType;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Entity
@Setter
@Getter
public class DiscountCode {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String code;
    private double discountValue;
    private boolean isActive;
    private DiscountType type;
    private LocalDateTime validFrom;
    private LocalDateTime validUntil;

    @OneToMany
    private List<Basket> baskets = new ArrayList<Basket>();

    public DiscountCode() {
    }

    public boolean isValid() {
        LocalDateTime now = LocalDateTime.now();
        return isActive && (now.isAfter(validFrom) && now.isBefore(validUntil));
    }
}
