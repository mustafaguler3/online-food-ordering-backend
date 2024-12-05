package com.example.food_ordering.util;

import com.example.food_ordering.enums.ProductCategory;
import org.springframework.core.convert.converter.Converter;
import org.springframework.stereotype.Component;

import java.lang.annotation.Annotation;


public class StringToProductCategoryConverter implements Converter<String, ProductCategory> {

    @Override
    public ProductCategory convert(String source) {
        try {
            return ProductCategory.valueOf(source.toUpperCase());
        } catch (IllegalArgumentException e) {
            throw new IllegalArgumentException("Invalid category: " + source);
        }
    }
}
