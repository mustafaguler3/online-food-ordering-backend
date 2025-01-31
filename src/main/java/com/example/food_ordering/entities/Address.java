package com.example.food_ordering.entities;

import com.example.food_ordering.enums.AddressType;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.Date;
import java.util.List;

@Entity
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class Address {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String firstName;
    private String lastName;
    private String addressLine1;
    private String addressLine2;
    private String state;
    private String city;
    private String country;
    private String zipCode;
    private String phone;
    @Enumerated(EnumType.STRING)
    private AddressType type;
    @ManyToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "user_id")
    private User user;
    @Temporal(TemporalType.TIMESTAMP)
    private Date createdDate;


    @OneToMany(mappedBy = "shippingAddress")
    private List<Order> orders;






}



















