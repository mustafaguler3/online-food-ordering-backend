package com.example.food_ordering.converter;

import com.example.food_ordering.dto.UserDto;
import com.example.food_ordering.entities.User;
import org.springframework.stereotype.Component;

@Component
public class UserConverter {

    public User toEntity(UserDto userDto){
        return User.builder()
                .username(userDto.getUsername())
                .email(userDto.getEmail())
                .password(userDto.getPassword())
                .phoneNumber(userDto.getPhoneNumber())
                .profileImage(userDto.getProfileImage())
                .firstName(userDto.getFirstName())
                .lastName(userDto.getLastName())
                .build();
    }

    public UserDto toDto(User user){
        return UserDto.builder()
               .id(user.getId())
               .username(user.getUsername())
               .email(user.getEmail())
               .password(user.getPassword())
               .phoneNumber(user.getPhoneNumber())
               .profileImage(user.getProfileImage())
               .firstName(user.getFirstName())
               .lastName(user.getLastName())
               .build();
    }

}
