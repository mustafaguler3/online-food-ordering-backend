package com.example.food_ordering.service;

import com.example.food_ordering.dto.BasketDto;
import com.example.food_ordering.dto.UserDto;

import java.util.Optional;

public interface BasketService {
    void addToCart(long productId, int quantity);
    BasketDto removeFromCart(long productId);
    BasketDto updateBasket(long productId, int quantity);
    BasketDto findBasketByUserId();
    boolean clearBasket();

}
