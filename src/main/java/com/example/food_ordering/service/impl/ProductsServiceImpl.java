package com.example.food_ordering.service.impl;

import com.example.food_ordering.dto.ProductDto;
import com.example.food_ordering.entities.Product;
import com.example.food_ordering.entities.Restaurant;
import com.example.food_ordering.repository.ProductRepository;
import com.example.food_ordering.repository.RestaurantRepository;
import com.example.food_ordering.service.ProductService;
import com.example.food_ordering.util.DTOConverter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class ProductsServiceImpl implements ProductService {

    @Autowired
    private ProductRepository productRepository;
    @Autowired
    private RestaurantRepository restaurantRepository;
    @Autowired
    private DTOConverter dtoConverter;

    @Override
    public List<ProductDto> getAllProducts() {
        var products = productRepository.getAllProducts();
        List<ProductDto> productDtos =
                products.stream().map(p -> dtoConverter.toProductDto(p)).toList();
        return productDtos;
    }

    @Override
    public ProductDto getProduct(int id) {
        var product = productRepository.findById((long) id).orElse(null);
        if (product!=null) {
            return dtoConverter.toProductDto(product);
        }
        return null;
    }

    @Override
    public List<ProductDto> getProductsByRestaurantId(int restaurantId) {
        // Check if restaurant exists
        Optional<Restaurant> restaurant =
                Optional.ofNullable(restaurantRepository.findById(restaurantId));
        if (restaurant.isEmpty()) {
            return List.of(); // Return an empty list instead of null
        }

        // Fetch products associated with the restaurant
        List<Product> products = productRepository.findByRestaurantId(restaurantId);

        // Map Product entities to ProductDto
        return products.stream()
                .map(product -> dtoConverter.toProductDto(product))
                .toList(); // Java 16+ toList(), or collect(Collectors.toList())
    }

    @Override
    public List<ProductDto> findByCategory(long categoryId) {
        // TODO: Implement this method to filter products by category
        List<Product> products = productRepository.findByCategoryId(categoryId);
        if (products.isEmpty()) {
            return List.of(); // Return an empty list instead of null
        }
        List<ProductDto> dtoList =
                products.stream().map(product -> dtoConverter.toProductDto(product)).collect(Collectors.toList());

        return dtoList;
    }
}




















