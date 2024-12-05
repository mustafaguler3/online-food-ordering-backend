package com.example.food_ordering.enums;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonValue;
import lombok.Data;
import lombok.Getter;


public enum ProductCategory {
    PIZZA,
    BURGER,
    WRAP,
    DRINKS,
    DESSERT,
    NOODLES,
    PASTA;

    @JsonCreator
    public static ProductCategory fromValue(String value) {
        for (ProductCategory category : values()) {
            if (category.name().equalsIgnoreCase(value)) {
                return category;
            }
        }
        throw new IllegalArgumentException("Invalid category: " + value);
    }
}
