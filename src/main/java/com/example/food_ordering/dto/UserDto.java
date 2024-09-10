package com.example.food_ordering.dto;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Builder
public class UserDto {
    private long id;
    private String username;
    private String email;
    private String password;
    private String phoneNumber;
    private String profileImage;
    private String firstName;
    private String lastName;
    private boolean isEnabled;
}
