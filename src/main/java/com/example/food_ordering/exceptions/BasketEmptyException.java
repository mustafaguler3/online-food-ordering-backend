package com.example.food_ordering.exceptions;

public class BasketEmptyException extends RuntimeException{
    public BasketEmptyException(String message) {
        super(message);
    }
}
