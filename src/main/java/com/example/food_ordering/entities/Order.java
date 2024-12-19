package com.example.food_ordering.entities;

import com.example.food_ordering.enums.OrderStatus;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.*;

@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "orders")
public class Order {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String orderReferenceNumber;
    private Double totalAmount;
    private OrderStatus status;
    @Temporal(TemporalType.TIMESTAMP)
    private Date orderDate;
    @OneToMany(mappedBy = "order", cascade = CascadeType.ALL)
    private List<OrderItem> items = new ArrayList<>();
    @ManyToOne
    @JoinColumn(name = "user_id")
    private User user;
    @OneToOne(mappedBy = "order",cascade = CascadeType.ALL)
    private Payment payment;
    @OneToOne
    @JoinColumn(name = "basket_id")
    private Basket basket;
    @OneToOne
    @JoinColumn(name = "shipping_address_id")
    private Address shippingAddress;
    @OneToOne
    @JoinColumn(name = "billing_address_id")
    private Address billingAddress;

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
