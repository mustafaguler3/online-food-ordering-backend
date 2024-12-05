package com.example.food_ordering.dto;

import lombok.Data;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Getter
@Setter
public class BasketDto {
    private int id;
    private List<BasketItemDto> items = new ArrayList<>();
    private double totalPrice;
    private double discount;
    private UserDto user;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
    private String status;  // 'active', 'inactive'

    public BasketDto() {
    }



}
