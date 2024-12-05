package com.example.food_ordering.service.impl;

import com.example.food_ordering.dto.BasketDto;
import com.example.food_ordering.entities.Basket;
import com.example.food_ordering.entities.BasketItem;
import com.example.food_ordering.entities.Product;
import com.example.food_ordering.entities.User;
import com.example.food_ordering.exceptions.BasketNotFoundException;
import com.example.food_ordering.exceptions.ProductNotFoundException;
import com.example.food_ordering.exceptions.UserNotFoundException;
import com.example.food_ordering.repository.BasketRepository;
import com.example.food_ordering.repository.ProductRepository;
import com.example.food_ordering.repository.UserRepository;
import com.example.food_ordering.service.BasketService;
import com.example.food_ordering.util.DTOConverter;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import java.util.Optional;

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

    @Override
    @Transactional
    public void addToCart(long userId, long productId, int quantity) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String currentUser = authentication.getName();

        User user = userRepository.findByUsername(currentUser);

        Product product = productRepository.findById(productId)
                .orElseThrow(() -> new ProductNotFoundException("Product"));

        Basket basket = basketRepository.findByUserId(userId)
                .orElseThrow(() -> new BasketNotFoundException("Basket not found"));

        if (basket == null){
            basket = new Basket();
            basket.setUser(user);
        }

        Optional<BasketItem> existingBasketItem = basket.getBasketItems()
                .stream()
                .filter(item -> item.getProduct().getId() == product.getId())
                .findFirst();

        if (existingBasketItem.isPresent()){
            existingBasketItem.get().setQuantity(existingBasketItem.get().getQuantity() + 1);
        }else {
            BasketItem basketItem = new BasketItem();
            basketItem.setProduct(product);
            basketItem.setBasket(basket);
            basketItem.setQuantity(quantity);
            basket.getBasketItems().add(basketItem);
        }
        basketRepository.save(basket);
    }

    @Override
    public BasketDto removeFromCart(long userId, long productId) {
        return null;
    }

    @Override
    public BasketDto updateCart(long userId, long productId, int quantity) {
        return null;
    }

    @Override
    public BasketDto getCartByUserId(long userId) throws Exception {
        Basket basket = basketRepository.findByUserId(userId)
                .orElseThrow(() -> new BasketNotFoundException("User basket is empty"));

        BasketDto basketDto = dtoConverter.toBasketDto(basket);
        return basketDto;
    }

    @Override
    public boolean clearCart(long userId) {
        return false;
    }
}
