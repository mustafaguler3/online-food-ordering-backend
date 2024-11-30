package com.example.food_ordering.controller;

import com.example.food_ordering.entities.Product;
import com.example.food_ordering.service.ProductService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api")
public class ProductsController {

    @Autowired
    private ProductService productService;

    @GetMapping("/products")
    public ResponseEntity<?> getProducts(){

        var products = productService.getAllProducts();

        return ResponseEntity.ok(products);
    }

    @GetMapping("/products/{id}")
    public ResponseEntity<?> getProduct(@PathVariable("id") int id){
        return ResponseEntity.ok(productService.getProduct(id));
    }
}
