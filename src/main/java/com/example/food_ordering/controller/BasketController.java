package com.example.food_ordering.controller;

import com.example.food_ordering.dto.BasketDto;
import com.example.food_ordering.dto.BasketItemDto;
import com.example.food_ordering.entities.Basket;
import com.example.food_ordering.requests.AddToBasketRequest;
import com.example.food_ordering.service.BasketService;
import com.example.food_ordering.service.UserDetailsImpl;
import com.example.food_ordering.util.CurrentUserProvider;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api")
public class BasketController {

    @Autowired
    private BasketService basketService;
    @Autowired
    private CurrentUserProvider currentUserProvider;

    @GetMapping("/basket")
    public ResponseEntity<?> getUserBasket(@RequestParam("userId") long userId) {
        BasketDto basketDto = basketService.findBasketByUserId(userId);
        return ResponseEntity.ok(basketDto);
    }

    @PostMapping("/basket/add")
    public ResponseEntity<?> addProductToBasket(@Validated @RequestBody AddToBasketRequest request){
        basketService.addToCart(request.getProductId(),request.getQuantity());

        return ResponseEntity.ok("Product added successfully to basket");
    }

    @DeleteMapping("/basket/delete")
    public ResponseEntity<?> removeProductFromBasket(@RequestParam long productId){
        basketService.removeFromCart(productId);
        return ResponseEntity.ok("Product deleted successfully");
    }

    @DeleteMapping("/basket/clear")
    public ResponseEntity<?> clearBasket(@RequestParam long userId){
        basketService.clearBasket(userId);
        return ResponseEntity.ok("Basket cleared successfully");
    }

}

























