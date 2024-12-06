package com.example.food_ordering.util;

import com.example.food_ordering.converter.UserConverter;
import com.example.food_ordering.dto.*;
import com.example.food_ordering.entities.*;
import com.example.food_ordering.repository.BasketRepository;
import com.example.food_ordering.repository.ProductRepository;
import com.example.food_ordering.repository.RestaurantRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Component
public class DTOConverter {

    @Autowired
    private RestaurantRepository restaurantRepository;
    @Autowired
    private UserConverter userConverter;
    @Autowired
    private ProductRepository productRepository;
    @Autowired
    private BasketRepository basketRepository;


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
        productDto.setCategory(product.getCategory().getName());
        productDto.setId(product.getId());
        productDto.setPrice(product.getPrice());
        productDto.setName(product.getName());
        productDto.setFoodImageUrls(product.getFoodImageUrls());
        productDto.setRestaurantId(product.getRestaurant().getId());

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
        //restaurant.setMaxDiscountAmount(restaurantDto.getMaxDiscountAmount());
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
        restaurantDto.setLocation(restaurant.getLocation());
        restaurantDto.setDeliveryTime(restaurant.getDeliveryTime());
        //restaurantDto.setMaxDiscountAmount(restaurant.getMaxDiscountAmount());
        restaurantDto.setRestaurantIcon(restaurant.getRestaurantIcon());

        List<ProductDto> products = restaurant.getProducts().stream().map(this::toProductDto).toList();

        restaurantDto.setProducts(products);

        return restaurantDto;
    }

    public Basket toBasketEntity(BasketDto basketDto) {
        Basket basket = new Basket();
        basket.setId(basketDto.getId());
        basket.setTotalPrice(basketDto.getTotalPrice());
        basket.setUser(userConverter.toEntity(basketDto.getUser()));
        basket.setBasketItems(basketDto.getItems().stream().map(this::toBasketItemEntity)
                .collect(Collectors.toSet()));

        return basket;
    }

    public BasketDto toBasketDto(Basket basket){
        BasketDto basketDto = new BasketDto();
        basketDto.setId(basket.getId());
        basketDto.setTotalPrice(basket.getTotalPrice());
        basketDto.setUser(userConverter.toDto(basket.getUser()));
        List<BasketItemDto> items =
                basket.getBasketItems().stream().map(this::toBasketItemDto).collect(Collectors.toList());
        basketDto.setItems(items);
        basketDto.setCreatedAt(basket.getCreatedAt());
        basketDto.setUpdatedAt(basket.getUpdatedAt());
        basketDto.setStatus("Active");
        return basketDto;
    }

    public BasketItem toBasketItemEntity(BasketItemDto basketItemDto){
        BasketItem basketItem = new BasketItem();
        basketItem.setId(basketItemDto.getId());
        Optional<Product> product = productRepository.findById(basketItemDto.getProductId());
        basketItem.setProduct(product.get());
        basketItem.setQuantity(basketItemDto.getQuantity());

        Basket basket = basketRepository.findById(basketItemDto.getBasketId()).get();

        basketItem.setBasket(basket);

        return basketItem;
    }

    public BasketItemDto toBasketItemDto(BasketItem basketItem){
        BasketItemDto basketItemDto = new BasketItemDto();
        basketItemDto.setId(basketItem.getId());
        ProductDto productDto = toProductDto(basketItem.getProduct());
        basketItemDto.setProductId((long) basketItem.getProduct().getId());
        basketItemDto.setQuantity(basketItem.getQuantity());
        basketItemDto.setBasketId(basketItem.getBasket().getId());
        return basketItemDto;
    }
}






















