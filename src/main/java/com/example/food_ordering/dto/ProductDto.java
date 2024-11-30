package com.example.food_ordering.dto;

import com.example.food_ordering.entities.Restaurant;
import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
public class ProductDto {
    private int id;
    private String name;
    private double price;
    private String description;
    private List<String> foodImageUrls;
    private String[] sizes;
    private String category;
    private String[] colors;
    private boolean isAvailable;
    private int quantity;
    private int restaurantId; // name is so important, it must be same in react
}
