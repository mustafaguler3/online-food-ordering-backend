package com.example.food_ordering.dto;

import com.example.food_ordering.entities.Product;
import jakarta.persistence.CascadeType;
import jakarta.persistence.FetchType;
import jakarta.persistence.OneToMany;
import lombok.Getter;
import lombok.Setter;

import java.util.ArrayList;
import java.util.List;

@Getter
@Setter
public class RestaurantDto {
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
    //private List<Product> products = new ArrayList<>();
}