package com.example.food_ordering.dto;

import com.example.food_ordering.entities.User;
import com.example.food_ordering.enums.AddressType;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.util.Date;

@Getter
@Setter
public class AddressDto {
    private String firstName;
    private String lastName;
    private String addressLine1;
    private String addressLine2;
    private String state;
    private String city;
    private String country;
    private String zipCode;
    private String phone;
    private AddressType type;
    private long userId;
}
