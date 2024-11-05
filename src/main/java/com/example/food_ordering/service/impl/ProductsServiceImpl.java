package com.example.food_ordering.service.impl;

import com.example.food_ordering.dto.ProductDto;
import com.example.food_ordering.entities.Product;
import com.example.food_ordering.repository.ProductRepository;
import com.example.food_ordering.service.ProductService;
import com.example.food_ordering.util.DTOConverter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ProductsServiceImpl implements ProductService {

    @Autowired
    private ProductRepository productRepository;
    @Autowired
    private DTOConverter dtoConverter;

    @Override
    public List<ProductDto> getAllProducts() {
        var products = productRepository.getAllProducts();

        List<ProductDto> productDtos =  products.stream().map(p -> dtoConverter.toProductDto(p)).toList();

        return productDtos;
    }
}
