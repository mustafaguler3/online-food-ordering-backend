package com.example.food_ordering.controller;

import com.example.food_ordering.dto.OrderDto;
import com.example.food_ordering.response.OrderResponseDto;
import com.example.food_ordering.service.OrderService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/order")
public class OrderController {
    @Autowired
    private OrderService orderService;

    @PostMapping("/create")
    public ResponseEntity<?> createOrder(@Valid @RequestBody OrderDto orderDto) {
        OrderDto order = orderService.createOrder(orderDto);

        return ResponseEntity.ok(order);
    }

    @GetMapping("/user")
    public ResponseEntity<?> getOrdersForUser() {
        List<OrderResponseDto> orderDtos = orderService.getUserOrders();

        return ResponseEntity.ok(orderDtos);
    }

    @GetMapping("/user/orders")
    public ResponseEntity<?> getUserOrder(@RequestParam long orderId) {
        List<OrderResponseDto> orderDtos = orderService.getUserOrders();
        for (OrderResponseDto order : orderDtos){
            if (order.getId().equals(orderId)){
                return ResponseEntity.ok(order);
            }
        }
        return ResponseEntity.badRequest().body("no found order for user");
    }
}
