package com.example.food_ordering.entities;

import com.example.food_ordering.enums.PaymentStatus;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;


import java.util.Date;

@Entity
@Setter
@Getter
@NoArgsConstructor
@AllArgsConstructor
public class Payment {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @OneToOne
    @JoinColumn(name = "order_id")
    private Order order;
    private String currency;
    @ManyToOne
    @JoinColumn(name = "user_id")
    private User user;
    private String paymentReferenceNumber;

    @ManyToOne
    @JoinColumn(name = "saved_card_id")
    private SavedCard savedCard;

    @Enumerated(EnumType.STRING)
    private PaymentStatus status;
    private Double amountPaid;
    private String paymentMethod;
    @Temporal(TemporalType.TIMESTAMP)
    private Date paymentDate;
    // Kredi kartı
    private String cardNumber;
    private String cardHolderName;
    private String expiryDate;
    private String cvv;
    // Banka transferi
    private String bankName;
    private String accountNumber;
    // PayPal
    private String paypalEmail;
}
