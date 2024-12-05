package com.example.food_ordering.service;

import com.example.food_ordering.dto.BasketDto;

import java.util.Optional;

public interface BasketService {
    void addToCart(long userId, long productId, int quantity);
    BasketDto removeFromCart(long userId,long productId);
    BasketDto updateCart(long userId,long productId, int quantity);
    BasketDto getCartByUserId(long userId) throws Exception;
    boolean clearCart(long userId);
}
