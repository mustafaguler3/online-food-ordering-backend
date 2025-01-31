package com.example.food_ordering.entities;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Setter
@Getter
@NoArgsConstructor
public class OrderItem {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @ManyToOne
    @JoinColumn(name = "order_id")
    private Order order;
    @ManyToOne
    @JoinColumn(name = "product_id")
    private Product product;
    private Integer quantity;
    private Double unitPrice;
    private Double totalPrice;

    private Double discountPercentage;
    private Double taxRate;


    public Double getTotalPrice() {
        double priceAfterDiscount = unitPrice - (unitPrice * (discountPercentage != null ? discountPercentage : 0) / 100);
        double priceAfterTax = priceAfterDiscount + (priceAfterDiscount * (taxRate != null ? taxRate : 0) / 100);
        return priceAfterTax * quantity;
    }

}
