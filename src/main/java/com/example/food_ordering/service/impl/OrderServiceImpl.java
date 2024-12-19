package com.example.food_ordering.service.impl;

import com.example.food_ordering.dto.OrderDto;
import com.example.food_ordering.entities.Order;
import com.example.food_ordering.entities.OrderItem;
import com.example.food_ordering.entities.Payment;
import com.example.food_ordering.enums.OrderStatus;
import com.example.food_ordering.enums.PaymentStatus;
import com.example.food_ordering.repository.OrderRepository;
import com.example.food_ordering.repository.PaymentRepository;
import com.example.food_ordering.response.OrderResponseDto;
import com.example.food_ordering.service.OrderService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
public class OrderServiceImpl implements OrderService {

    @Autowired
    private OrderRepository orderRepository;
    @Autowired
    private PaymentRepository paymentRepository;

    @Override
    public void createOrder(OrderDto orderDto) {
        // 1. Sipariş oluşturma
        /*Order order = new Order();
        order.setUserId(orderRequestDto.getUserId());
        order.setTotalAmount(orderRequestDto.getTotalAmount());
        order.setStatus(OrderStatus.PENDING);
        Order savedOrder = orderRepository.save(order);

        // 2. Ödeme işlemi
        paymentService.processPayment(savedOrder, orderRequestDto.getPayment());

        // 3. Sipariş durumu güncelleme
        savedOrder.setStatus(OrderStatus.PROCESSING);
        orderRepository.save(savedOrder);

        // 4. DTO dönüşü
        return new OrderResponseDto(savedOrder.getId(), savedOrder.getStatus()); */
    }

    @Override
    public OrderDto getOrderById(Long orderId) {
        return null;
    }

    @Override
    public OrderDto updateOrderStatus(Long orderId, String status) {
        return null;
    }
}





















