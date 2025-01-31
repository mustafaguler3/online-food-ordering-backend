package com.example.food_ordering.repository;

import com.example.food_ordering.entities.Order;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface OrderRepository extends JpaRepository<Order,Long> {
    List<Order> findOrdersByUserId(long userId);
    Order findOrderById(long orderId);
}
