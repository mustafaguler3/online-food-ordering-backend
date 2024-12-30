package com.example.food_ordering.service.impl;

import com.example.food_ordering.dto.PaymentDto;
import com.example.food_ordering.entities.Order;
import com.example.food_ordering.entities.Payment;
import com.example.food_ordering.enums.PaymentStatus;
import com.example.food_ordering.repository.PaymentRepository;
import com.example.food_ordering.service.PaymentService;
import com.example.food_ordering.util.CreditCardValidator;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.Objects;
import java.util.UUID;

@Service
public class PaymentServiceImpl implements PaymentService {

    @Autowired
    private PaymentRepository paymentRepository;

    @Override
    public boolean processPayment(PaymentDto paymentDto, Order order) {
        Payment payment = new Payment();
        payment.setStatus(PaymentStatus.PENDING);
        payment.setAmountPaid(order.getTotalAmount());
        payment.setPaymentMethod(paymentDto.getPaymentMethod());
        payment.setPaymentDate(new Date());
        payment.setPaymentReferenceNumber(UUID.randomUUID().toString());

        if(Objects.equals(payment.getPaymentMethod(), "Credit Card")) {
            payment.setCardNumber(paymentDto.getCardNumber());
            payment.setCvv(paymentDto.getCvv());
            payment.setExpiryDate(paymentDto.getExpirationDate());
            payment.setCardHolderName(paymentDto.getCardHolderName());
        }

        boolean paymentSuccessful = processExternalPayment(payment);

        if (paymentSuccessful) {
            payment.setStatus(PaymentStatus.COMPLETED);
            payment.setOrder(order);
            paymentRepository.save(payment);
            return true;
        } else {
            payment.setStatus(PaymentStatus.FAILED);
            paymentRepository.save(payment);
            return false;
        }

    }

    private boolean processExternalPayment(Payment payment) {
        return true;
    }

    private void validateCreditCart(PaymentDto paymentDto){
        if(!CreditCardValidator.isValidCardNumber(paymentDto.getCardNumber())){
            throw new IllegalArgumentException("Invalid card number");
        }
        if(!CreditCardValidator.isValidCvv(paymentDto.getCvv())){
            throw new IllegalArgumentException("Invalid CVV");
        }
        if(!CreditCardValidator.isValidExpirationDate(paymentDto.getExpirationDate())){
            throw new IllegalArgumentException("Invalid expiration date");
        }
    }
    private String maskCardNumber(String cardNumber){
        return cardNumber.replaceAll("\\d{12}(\\d{4})","************$1");
    }
}
