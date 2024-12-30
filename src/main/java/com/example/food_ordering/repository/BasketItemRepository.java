package com.example.food_ordering.repository;

import com.example.food_ordering.entities.Basket;
import com.example.food_ordering.entities.BasketItem;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface BasketItemRepository extends JpaRepository<BasketItem,Long> {
    void deleteAllByBasket(Basket basket);
}
