package com.example.food_ordering.repository;

import com.example.food_ordering.entities.Product;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ProductRepository extends JpaRepository<Product,Long> {
    @Query("SELECT p FROM Product p")
    List<Product> getAllProducts();
    List<Product> findByRestaurantId(int restaurantId);
    List<Product> findByCategoryId(long categoryId);
}
