package com.example.food_ordering.service.impl;

import com.example.food_ordering.dto.BasketDto;
import com.example.food_ordering.dto.BasketItemDto;
import com.example.food_ordering.entities.*;
import com.example.food_ordering.enums.DiscountType;
import com.example.food_ordering.exceptions.BasketEmptyException;
import com.example.food_ordering.exceptions.BasketNotFoundException;
import com.example.food_ordering.exceptions.ProductNotFoundException;
import com.example.food_ordering.exceptions.UserNotFoundException;
import com.example.food_ordering.repository.BasketItemRepository;
import com.example.food_ordering.repository.BasketRepository;
import com.example.food_ordering.repository.ProductRepository;
import com.example.food_ordering.repository.UserRepository;
import com.example.food_ordering.service.BasketService;
import com.example.food_ordering.service.DiscountCodeService;
import com.example.food_ordering.service.UserDetailsImpl;
import com.example.food_ordering.util.CurrentUserProvider;
import com.example.food_ordering.util.DTOConverter;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.elasticsearch.ResourceNotFoundException;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.concurrent.ThreadLocalRandom;
import java.util.stream.Collectors;

@Service
public class BasketServiceImpl implements BasketService {

    @Autowired
    private BasketRepository basketRepository;
    @Autowired
    private ProductRepository productRepository;
    @Autowired
    private DiscountCodeService discountCodeService;
    @Autowired
    private UserRepository userRepository;
    @Autowired
    private DTOConverter dtoConverter;
    @Autowired
    private BasketItemRepository basketItemRepository;
    @Autowired
    private CurrentUserProvider currentUserProvider;

    @Override
    @Transactional
    public void addToCart(long productId, int quantity) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        System.out.println("Authentication type: " + authentication.getPrincipal().getClass().getName());
        System.out.println(authentication.getPrincipal().getClass().getName());

        UserDetailsImpl userDetails = (UserDetailsImpl) authentication.getPrincipal();

        Product product = productRepository.findById(productId)
                .orElseThrow(() -> new ProductNotFoundException("Product"));

        User user = userRepository.findById(userDetails.user.getId()).get();

        Basket basket = basketRepository.findByUserId(user.getId())
                .orElseGet(() -> {
                    Basket newBasket = new Basket();
                    newBasket.setUser(user);
                    newBasket.setCreatedAt(LocalDateTime.now());
                    newBasket.setCurrency("USD");
                    newBasket.setStatus("Active");

                    newBasket.calculateTotals();
                    basketRepository.save(newBasket);
                    return newBasket;
                });

        BasketItem existingCart =
                basket.getBasketItems()
                .stream()
                .filter(item -> item.getProduct().getId() == product.getId())
                .findFirst().orElse(null);

        if (existingCart != null){
            existingCart.setQuantity(existingCart.getQuantity() + quantity);
            existingCart.calculateTotalPrice();
        }else {
            BasketItem basketItem = new BasketItem();
            basketItem.setProduct(product);
            basketItem.setBasket(basket);
            basketItem.setQuantity(quantity);
            basketItem.setDiscount(5);
            basketItem.setUnitPrice(product.getPrice());
            basketItem.calculateDiscountPercentage();
            basketItem.calculateTotalPrice();
            basket.getBasketItems().add(basketItem);
        }

        basket.calculateTotals();
        basketRepository.save(basket);
    }

    @Override
    public BasketDto removeFromCart(long productId) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        UserDetailsImpl userDetails = (UserDetailsImpl) authentication.getPrincipal();

        Basket basket = basketRepository.findByUserId(userDetails.user.getId()).get();

        BasketItem basketItem = basket.getBasketItems()
                .stream()
                .filter(item -> item.getProduct().getId() == productId)
                .findFirst()
                .orElseThrow(() -> new ProductNotFoundException("Product not found in basket"));

        basketItem.setQuantity(basketItem.getQuantity() - 1);
        basketItem.calculateTotalPrice();
        basket.getBasketItems().remove(basketItem);

        basket.calculateTotals();
        basketRepository.save(basket);

        return dtoConverter.toBasketDto(basket);
    }

    @Override
    @Transactional
    @Modifying
    public BasketDto updateBasket(int productId, int quantity) {
        UserDetailsImpl userDetails = currentUserProvider.getCurrentUserDetails();

        if (userDetails == null || userDetails.user == null) {
            throw new RuntimeException("User is not authenticated");
        }

        Basket basket = basketRepository.findByUserId(userDetails.user.getId())
                .orElse(null);

        BasketItem item =
                basket.getBasketItems()
                .stream()
                .filter(basketItem -> basketItem.getProduct().getId() == productId)
                .findFirst()
                        .orElse(null);

        if (quantity <= 0) {
            throw new IllegalArgumentException("Quantity must be greater than zero.");
        }

        if (item != null){
            item.setQuantity(quantity);
            item.calculateTotalPrice();
            basket.calculateTotals();
            basketRepository.save(basket);
        }

        return dtoConverter.toBasketDto(basket);
    }

    @Override
    public BasketDto findBasketByUserId() {
        UserDetailsImpl userDetails = currentUserProvider.getCurrentUserDetails();

        if (userDetails == null || userDetails.user == null) {
            throw new RuntimeException("User is not authenticated");
        }

        Basket basket = basketRepository.findByUserId(userDetails.user.getId())
                .orElse(null);

        if (basket == null) {
            return new BasketDto(0, new ArrayList<>(), 0.0, null);
        }
        if (basket.getBasketItems().isEmpty()) {
            return new BasketDto(basket.getId(), new ArrayList<>(), 0.0,
                    dtoConverter.toDto(basket.getUser()));
        }
        List<BasketItemDto> basketItems =
                basket.getBasketItems()
                .stream()
                .map(basketItem -> dtoConverter.toBasketItemDto(basketItem)
                )
                .collect(Collectors.toList());

        double totalPrice =
                basketItems.stream()
                .mapToDouble(item -> (item.getUnitPrice() * item.getQuantity()) - item.getDiscount())
                .sum();

        BasketDto basketDto = dtoConverter.toBasketDto(basket);
        basketDto.setItems(basketItems);
        basketDto.setTotalPrice(totalPrice);

        return basketDto;
    }

    @Override
    @Transactional
    public boolean clearBasket() {
        UserDetailsImpl userDetails = currentUserProvider.getCurrentUserDetails();
        Basket basket = basketRepository.findByUserId(userDetails.user.getId()).get();

        List<BasketItem> basketItems = basket.getBasketItems();
        if (basketItems != null && !basketItems.isEmpty()) {
            basketItemRepository.deleteAll(basketItems);
        }

        if (basket != null) {
            basket.getBasketItems().clear();
            basketRepository.save(basket);
        }


        return true;
    }

    @Override
    public BasketDto applyDiscountCodeToBasket(String discountCode) {
        UserDetailsImpl userDetails = currentUserProvider.getCurrentUserDetails();
        if (userDetails == null || userDetails.user == null) {
            throw new RuntimeException("User is not authenticated");
        }
        Basket basket = basketRepository.findByUserId(userDetails.user.getId()).orElse(null);
        DiscountCode code = discountCodeService.getDiscountCodeByCode(discountCode);
        if (code != null && code.isValid()){
            basket.setDiscountCode(code);
            basket.setDiscount(code.getDiscountValue());
            basket.calculateTotals();
        }else {
            throw new RuntimeException("Invalid or expired discount code");
        }
        basketRepository.save(basket);
        return dtoConverter.toBasketDto(basket);
    }

    private double calculateDiscount(double totalPrice,DiscountCode code){
        if (code.getType() == DiscountType.PERCENTAGE){
            return totalPrice * (code.getDiscountValue() / 100);
        }else {
            return code.getDiscountValue();
        }
    }

    public String generateRandomBasketId() {
        int randomId = ThreadLocalRandom.current().nextInt(100000, 999999);
        return "BASKET-" + randomId;
    }
}























