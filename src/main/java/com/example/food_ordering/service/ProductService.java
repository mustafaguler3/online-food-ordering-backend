package com.example.food_ordering.service;


import com.example.food_ordering.dto.ProductDto;
import com.example.food_ordering.entities.Product;

import java.util.List;

public interface ProductService {
    List<ProductDto> getAllProducts();
}