package com.example.food_ordering.dto;

import lombok.*;

import java.util.ArrayList;
import java.util.List;

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
    private List<AddressDto> addresses = new ArrayList<AddressDto>();
    private List<SavedCardDto> cards = new ArrayList<SavedCardDto>();

    public UserDto(long id, String username, String email, String password, String phoneNumber, String profileImage, String firstName, String lastName) {
        this.id = id;
        this.username = username;
        this.email = email;
        this.password = password;
        this.phoneNumber = phoneNumber;
        this.profileImage = profileImage;
        this.firstName = firstName;
        this.lastName = lastName;
    }
}
