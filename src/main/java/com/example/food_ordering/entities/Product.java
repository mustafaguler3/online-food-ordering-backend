package com.example.food_ordering.entities;

import com.example.food_ordering.enums.ProductCategory;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Entity
@Getter
@Setter
public class Product {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;
    private String name;
    private double price;
    private String description;
    @ElementCollection
    private List<String> foodImageUrls;
    @ManyToOne(cascade = CascadeType.ALL,fetch = FetchType.LAZY)
    @JoinColumn(name = "restaurant_id")
    private Restaurant restaurant;

    @ElementCollection
    @CollectionTable(name = "product_sizes", joinColumns = @JoinColumn(name = "product_id"))
    private List<String> sizes; // like S, M, L, XL

    @Enumerated(EnumType.STRING)
    private ProductCategory category; // like Pizza, Burger, Döner

    @ElementCollection
    @CollectionTable(name = "product_colors", joinColumns = @JoinColumn(name = "product_id"))
    private List<String> colors; // Ör: for different color options (Opsiyonel)

    private boolean isAvailable;

    private int quantity;

    public Product(int id, String name, double price, String description, List<String> foodImageUrls) {
        this.id = id;
        this.name = name;
        this.price = price;
        this.description = description;
        this.foodImageUrls = foodImageUrls;
    }

    public Product() {
    }
}























