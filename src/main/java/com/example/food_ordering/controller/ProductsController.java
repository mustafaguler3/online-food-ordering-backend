package com.example.food_ordering.controller;

import com.example.food_ordering.dto.ProductDto;
import com.example.food_ordering.entities.Product;
import com.example.food_ordering.enums.ProductCategory;
import com.example.food_ordering.service.ProductService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
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

    @GetMapping("/products/category")
    public ResponseEntity<?> getProductsByCategory(@RequestParam("categoryId") long categoryId){
        try {
            List<ProductDto> products = productService.findByCategory(categoryId);

            return ResponseEntity.ok(products);
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().body("Invalid category: " + categoryId);
        }
    }
}
