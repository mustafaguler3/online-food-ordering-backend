package com.example.food_ordering.repository;

import com.example.food_ordering.entities.Basket;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface BasketRepository extends JpaRepository<Basket,Long> {
    Optional<Basket> findByUserId(long userId);
}
