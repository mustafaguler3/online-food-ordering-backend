package com.example.food_ordering.entities;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.util.ArrayList;
import java.util.List;


@Entity
@Setter
@Getter
public class Restaurant {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;
    private String name;
    private double rating;
    private String location;
    private double distance; // in km
    private int deliveryTime; // in minutes
    private boolean bestSeller;
    private String discountDescription;
    private double discountPercent;
    private double maxDiscountAmount;
    private String restaurantIcon;
    @OneToMany(mappedBy = "restaurant",cascade = CascadeType.ALL,fetch = FetchType.LAZY)
    private List<Product> products = new ArrayList<>();


    public Restaurant() {
    }
}

























