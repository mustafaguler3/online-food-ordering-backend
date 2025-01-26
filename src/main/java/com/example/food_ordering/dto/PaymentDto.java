package com.example.food_ordering.dto;

import com.example.food_ordering.entities.Order;
import com.example.food_ordering.enums.PaymentStatus;
import jakarta.persistence.*;
import lombok.Data;
import lombok.Getter;
import lombok.Setter;

import java.util.Date;
import java.util.List;

@Getter
@Setter
public class PaymentDto {
    private Long id;
    private Long orderId;
    private String paymentReferenceNumber;
    @Enumerated(EnumType.STRING)
    private PaymentStatus status;
    private Double amountPaid;
    private String currency;
    private String paymentMethod;
    private Long userId;
    @Temporal(TemporalType.TIMESTAMP)
    private Date paymentDate;
    private String cardNumber;
    private String cardHolderName;
    private String expirationDate;
    private String cvv;
}
