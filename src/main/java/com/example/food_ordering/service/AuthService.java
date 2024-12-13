package com.example.food_ordering.service;

import com.example.food_ordering.dto.TokenDto;
import com.example.food_ordering.dto.UserDto;
import com.example.food_ordering.entities.User;
import org.springframework.web.multipart.MultipartFile;

public interface AuthService {
    TokenDto login(UserDto userDto);
    void register(UserDto userDto, MultipartFile profilePicture);
    UserDetailsImpl findByUsername(String username);
    UserDto findByEmail(String email);
    boolean verifyUser(String token);
}
