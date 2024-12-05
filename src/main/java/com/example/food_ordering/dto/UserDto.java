package com.example.food_ordering.dto;

import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
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
