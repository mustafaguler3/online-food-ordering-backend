package com.example.food_ordering.service.impl;

import com.example.food_ordering.converter.UserConverter;
import com.example.food_ordering.dto.BasketDto;
import com.example.food_ordering.dto.BasketItemDto;
import com.example.food_ordering.dto.UserDto;
import com.example.food_ordering.entities.Basket;
import com.example.food_ordering.entities.BasketItem;
import com.example.food_ordering.entities.Product;
import com.example.food_ordering.entities.User;
import com.example.food_ordering.exceptions.BasketEmptyException;
import com.example.food_ordering.exceptions.BasketNotFoundException;
import com.example.food_ordering.exceptions.ProductNotFoundException;
import com.example.food_ordering.exceptions.UserNotFoundException;
import com.example.food_ordering.repository.BasketRepository;
import com.example.food_ordering.repository.ProductRepository;
import com.example.food_ordering.repository.UserRepository;
import com.example.food_ordering.service.BasketService;
import com.example.food_ordering.service.UserDetailsImpl;
import com.example.food_ordering.util.CurrentUserProvider;
import com.example.food_ordering.util.DTOConverter;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
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
    private UserRepository userRepository;
    @Autowired
    private DTOConverter dtoConverter;
    @Autowired
    private UserConverter userConverter;
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
                .filter(item -> item.getProduct().getId()== product.getId())
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
    public BasketDto updateBasket(long userId, long productId, int quantity) {
        return null;
    }

    @Override
    public BasketDto findBasketByUserId(long userId) {
        // Kullanıcının sepetini bul
        Basket basket = basketRepository.findByUserId(userId).orElse(null);
        // Eğer sepet yoksa boş bir DTO dön
        if (basket == null) {
            return new BasketDto(0, new ArrayList<>(), 0.0, null);
        }
        // Sepet boşsa direkt DTO dön
        if (basket.getBasketItems().isEmpty()) {
            return new BasketDto(basket.getId(), new ArrayList<>(), 0.0, userConverter.toDto(basket.getUser()));
        }
        // Sepet öğelerini DTO'ya dönüştür
        List<BasketItemDto> basketItems =
                basket.getBasketItems()
                .stream()
                .map(basketItem -> dtoConverter.toBasketItemDto(basketItem)
                )
                .collect(Collectors.toList());

        // Toplam fiyatı hesapla
        double totalPrice =
                basketItems.stream()
                .mapToDouble(item -> item.getPrice() * item.getQuantity())
                .sum();

        BasketDto basketDto = dtoConverter.toBasketDto(basket);
        basketDto.setItems(basketItems);
        basketDto.setTotalPrice(totalPrice);
        return basketDto;
    }

    @Override
    public boolean clearBasket(long userId) {
        UserDetailsImpl userDetails = currentUserProvider.getCurrentUserDetails();

        Basket basket = basketRepository.findByUserId(userDetails.user.getId())
                .orElseThrow(() -> new BasketNotFoundException("Your basket is empty"));

        if (basket.getBasketItems().isEmpty()){
            return false;
        }
        basket.getBasketItems().clear();
        basket.calculateTotals();
        basketRepository.delete(basket);
        return true;
    }

    public String generateRandomBasketId() {
        int randomId = ThreadLocalRandom.current().nextInt(100000, 999999);
        return "BASKET-" + randomId;
    }
}
