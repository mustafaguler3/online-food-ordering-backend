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
    @OneToMany(mappedBy = "order", cascade = CascadeType.ALL,fetch = FetchType.EAGER)
    private List<OrderItem> items = new ArrayList<>();
    @ManyToOne
    @JoinColumn(name = "user_id")
    private User user;

    @ManyToOne
    @JoinColumn(name = "restaurant_id")
    private Restaurant restaurant;

    @OneToMany(mappedBy = "order",cascade = CascadeType.ALL,fetch = FetchType.LAZY)
    private List<Payment> payments;

    @ManyToOne
    @JoinColumn(name = "basket_id",unique = false)
    private Basket basket;

    @ManyToOne
    @JoinColumn(name = "shipping_address_id")
    private Address shippingAddress;
    @ManyToOne
    @JoinColumn(name = "billing_address_id")
    private Address billingAddress;

}
