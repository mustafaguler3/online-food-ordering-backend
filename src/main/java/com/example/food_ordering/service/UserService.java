package com.example.food_ordering.service;

import com.example.food_ordering.dto.UserDto;

import java.util.Optional;

public interface UserService {
    UserDto getUserProfile(Long userId);
}
