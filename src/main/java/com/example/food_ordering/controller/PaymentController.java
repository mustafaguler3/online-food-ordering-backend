package com.example.food_ordering.controller;

import com.example.food_ordering.dto.OrderDto;
import com.example.food_ordering.dto.PaymentDto;
import com.example.food_ordering.entities.Payment;
import com.example.food_ordering.repository.PaymentRepository;
import com.example.food_ordering.service.OrderService;
import com.example.food_ordering.service.PaymentService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api")
public class PaymentController {

    @Autowired
    private PaymentService paymentService;
    @Autowired
    private OrderService orderService;

    @PostMapping("/payment/add")
    public ResponseEntity<?> addPayment(@RequestBody PaymentDto paymentDto){
        try {
            // Order bilgisi örnek olarak alınıyor; gerçek uygulamada bunu bir servisten alın.
            OrderDto order = orderService.getOrderById(paymentDto.getOrderId());

            if (order == null) {
                return new ResponseEntity<>("Order not found", HttpStatus.NOT_FOUND);
            }

            boolean isPaymentSuccessful = paymentService.processPayment(paymentDto, order);

            if (isPaymentSuccessful) {
                return new ResponseEntity<>("Payment processed successfully", HttpStatus.OK);
            } else {
                return new ResponseEntity<>("Payment failed", HttpStatus.BAD_REQUEST);
            }
        } catch (Exception e) {
            return new ResponseEntity<>("An error occurred: " + e.getMessage(),
                    HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }
}
