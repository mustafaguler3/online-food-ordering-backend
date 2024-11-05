package com.example.food_ordering.util;

import com.example.food_ordering.dto.ProductDto;
import com.example.food_ordering.dto.RestaurantDto;
import com.example.food_ordering.entities.Product;
import com.example.food_ordering.entities.Restaurant;
import com.example.food_ordering.repository.RestaurantRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.Optional;

@Component
public class DTOConverter {

    @Autowired
    private RestaurantRepository restaurantRepository;

    public Product toProduct(ProductDto productDto) {
        Product product = new Product();
        product.setDescription(productDto.getDescription());
        product.setId(productDto.getId());
        product.setPrice(productDto.getPrice());
        product.setName(productDto.getName());
        product.setFoodImageUrls(productDto.getFoodImageUrls());


        return product;
    }

    public ProductDto toProductDto(Product product) {
        ProductDto productDto = new ProductDto();
        productDto.setDescription(product.getDescription());

        productDto.setId(product.getId());
        productDto.setPrice(product.getPrice());
        productDto.setName(product.getName());
        productDto.setFoodImageUrls(product.getFoodImageUrls());

        Restaurant restaurant =
                restaurantRepository.findById(product.getRestaurant().getId());

        var restaurantDto = toRestaurantDto(restaurant);

        productDto.setRestaurant(restaurantDto);

        return productDto;
    }

    public Restaurant toRestaurant(RestaurantDto restaurantDto) {
        Restaurant restaurant = new Restaurant();
        restaurant.setId(restaurantDto.getId());
        restaurant.setName(restaurantDto.getName());
        restaurant.setRating(restaurantDto.getRating());
        restaurant.setBestSeller(restaurantDto.isBestSeller());
        restaurant.setDiscountDescription(restaurantDto.getDiscountDescription());
        restaurant.setDiscountPercent(restaurantDto.getDiscountPercent());
        restaurant.setDistance(restaurantDto.getDistance());
        restaurant.setDeliveryTime(restaurantDto.getDeliveryTime());
        restaurant.setMaxDiscountAmount(restaurantDto.getMaxDiscountAmount());
        restaurant.setRestaurantIcon(restaurantDto.getRestaurantIcon());

        return restaurant;
    }

    public RestaurantDto toRestaurantDto(Restaurant restaurant) {
        RestaurantDto restaurantDto = new RestaurantDto();
        restaurantDto.setId(restaurant.getId());
        restaurantDto.setName(restaurant.getName());
        restaurantDto.setRating(restaurant.getRating());
        restaurantDto.setBestSeller(restaurant.isBestSeller());
        restaurantDto.setDiscountDescription(restaurant.getDiscountDescription());
        restaurantDto.setDiscountPercent(restaurant.getDiscountPercent());
        restaurantDto.setDistance(restaurant.getDistance());
        restaurantDto.setDeliveryTime(restaurant.getDeliveryTime());
        restaurantDto.setMaxDiscountAmount(restaurant.getMaxDiscountAmount());
        restaurantDto.setRestaurantIcon(restaurant.getRestaurantIcon());

        return restaurantDto;
    }
}
