package com.example.food_ordering.controller;

import com.example.food_ordering.service.DiscountCodeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api")
public class DiscountController {

    @Autowired
    private DiscountCodeService discountCodeService;

    @GetMapping("/discountCodes")
    public ResponseEntity<?> getDiscountCode(){
        return ResponseEntity.ok(discountCodeService.getAllDiscountCodes());
    }
}
