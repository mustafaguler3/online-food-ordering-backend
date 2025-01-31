package com.example.food_ordering.service.impl;

import com.example.food_ordering.dto.OrderDto;
import com.example.food_ordering.dto.PaymentDto;
import com.example.food_ordering.entities.Order;
import com.example.food_ordering.entities.Payment;
import com.example.food_ordering.entities.SavedCard;
import com.example.food_ordering.enums.PaymentStatus;
import com.example.food_ordering.repository.PaymentRepository;
import com.example.food_ordering.repository.SavedCardRepository;
import com.example.food_ordering.repository.UserRepository;
import com.example.food_ordering.service.PaymentService;
import com.example.food_ordering.util.CreditCardValidator;
import com.example.food_ordering.util.DTOConverter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.Objects;
import java.util.UUID;

@Service
public class PaymentServiceImpl implements PaymentService {

    @Autowired
    private PaymentRepository paymentRepository;
    @Autowired
    private DTOConverter dtoConverter;
    @Autowired
    private SavedCardRepository savedCardRepository;
    @Autowired
    private UserRepository userRepository;

    @Override
    public boolean processPayment(PaymentDto paymentDto, OrderDto orderDto) {
        Payment payment = new Payment();
        payment.setStatus(PaymentStatus.PENDING);
        payment.setAmountPaid(orderDto.getTotalAmount());
        payment.setPaymentMethod(paymentDto.getPaymentMethod());
        payment.setPaymentDate(new Date());
        payment.setPaymentReferenceNumber(UUID.randomUUID().toString());

        if ("Credit Card".equals(paymentDto.getPaymentMethod()) && paymentDto.getCardNumber() != null) {
            payment.setCardNumber(paymentDto.getCardNumber());
            payment.setCvv(paymentDto.getCvv());
            payment.setExpiryDate(paymentDto.getExpirationDate());
            payment.setCardHolderName(paymentDto.getCardHolderName());
            payment.setUser(userRepository.findById(orderDto.getUserId())
                    .orElseThrow(() -> new IllegalArgumentException("Kullanıcı bulunamadı")));
        }

        else if ("Saved Card".equals(paymentDto.getPaymentMethod())) {
            SavedCard savedCard = savedCardRepository.findSavedCardByUserId(orderDto.getUserId());
            if (savedCard != null) {
                payment.setCardNumber(savedCard.getMaskedCardNumber());
                payment.setCvv(savedCard.getCvv());
                payment.setExpiryDate(savedCard.getExpiryDate());
                payment.setCardHolderName(savedCard.getCardHolderName());

                payment.setUser(userRepository.findById(orderDto.getUserId())
                        .orElseThrow(() -> new IllegalArgumentException("Kullanıcı bulunamadı")));
            } else {
                throw new IllegalArgumentException("Kayıtlı kart bulunamadı.");
            }
        } else {
            throw new IllegalArgumentException("Geçerli bir ödeme yöntemi belirtilmedi.");
        }

        boolean paymentSuccessful = processExternalPayment(payment);

        if (paymentSuccessful) {
            payment.setStatus(PaymentStatus.COMPLETED);
            payment.setOrder(dtoConverter.mapToOrderEntity(orderDto));
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
