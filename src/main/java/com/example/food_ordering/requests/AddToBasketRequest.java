package com.example.food_ordering.requests;

import lombok.Getter;
import lombok.Setter;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;

@Getter
@Setter
public class AddToBasketRequest {
    @NotNull(message = "Product ID cannot be null")
    @Min(value = 1, message = "Product ID must be a positive number")
    private Long productId;

    @NotNull(message = "Quantity cannot be null")
    @Min(value = 1, message = "Quantity must be at least 1")
    private Integer quantity;
}
