package com.example.food_ordering.controller;

import com.example.food_ordering.service.BasketService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api")
public class BasketController {

    @Autowired
    private BasketService basketService;

    @GetMapping("/basket")
    public ResponseEntity<?> getUserBasket(@RequestParam long userId) {
        try {
            return ResponseEntity.ok(basketService.getCartByUserId(userId));
        } catch (Exception e) {
            return ResponseEntity.status(500).body(e.getMessage());
        }
    }
}

























