package com.example.food_ordering.exceptions;

public class BasketNotFoundException extends RuntimeException{
    public BasketNotFoundException(String message) {
        super(message);
    }
}
