package com.example.food_ordering.exceptions;

public class SavedCardNotFoundException extends RuntimeException {
    public SavedCardNotFoundException(String message) {
        super(message);
    }
}
