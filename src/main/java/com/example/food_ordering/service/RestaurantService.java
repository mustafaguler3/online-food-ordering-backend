package com.example.food_ordering.service;

import com.example.food_ordering.dto.RestaurantDto;
import com.example.food_ordering.entities.Restaurant;

import java.util.List;

public interface RestaurantService {
    List<RestaurantDto> getRestaurants();
    RestaurantDto getRestaurantById(Long id);
}
