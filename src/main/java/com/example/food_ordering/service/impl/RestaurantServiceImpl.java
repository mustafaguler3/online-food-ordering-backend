package com.example.food_ordering.service.impl;

import com.example.food_ordering.dto.RestaurantDto;
import com.example.food_ordering.entities.Restaurant;
import com.example.food_ordering.repository.ProductRepository;
import com.example.food_ordering.repository.RestaurantRepository;
import com.example.food_ordering.service.RestaurantService;
import com.example.food_ordering.util.DTOConverter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class RestaurantServiceImpl implements RestaurantService {

    @Autowired
    private RestaurantRepository restaurantRepository;
    @Autowired
    private ProductRepository productRepository;
    @Autowired
    private DTOConverter dtoConverter;

    @Override
    public List<RestaurantDto> getRestaurants() {
        List<Restaurant> restaurants = restaurantRepository.findAll();

        var dtos = restaurants
                .stream()
                .map((restaurantDto) -> dtoConverter.toRestaurantDto(restaurantDto))
                .toList();

        return dtos;
    }

    @Override
    public RestaurantDto getRestaurantById(Long id) {
        Restaurant restaurant = restaurantRepository.findById(id.intValue());
        if (restaurant != null){
            return dtoConverter.toRestaurantDto(restaurant);
        }
        return null;
    }
}
























