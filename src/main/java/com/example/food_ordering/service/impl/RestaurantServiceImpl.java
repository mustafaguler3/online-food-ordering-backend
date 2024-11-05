package com.example.food_ordering.service.impl;

import com.example.food_ordering.entities.Restaurant;
import com.example.food_ordering.repository.RestaurantRepository;
import com.example.food_ordering.service.RestaurantService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class RestaurantServiceImpl implements RestaurantService {

    @Autowired
    private RestaurantRepository restaurantRepository;

    @Override
    public List<Restaurant> getRestaurants() {
        return restaurantRepository.findAll();
    }
}
























