package com.example.food_ordering.service;

import com.example.food_ordering.dto.UserDto;

public interface EmailService {
    void sendVerificationEmail(UserDto userDto, String token);
}
