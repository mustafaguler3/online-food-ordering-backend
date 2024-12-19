package com.example.food_ordering.service.impl;

import com.example.food_ordering.dto.UserDto;
import com.example.food_ordering.entities.User;
import com.example.food_ordering.repository.UserRepository;
import com.example.food_ordering.service.UserService;
import com.example.food_ordering.util.DTOConverter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class UserServiceImpl implements UserService {

    @Autowired
    private UserRepository userRepository;
    @Autowired
    private DTOConverter dtoConverter;

    public UserDto getUserProfile(Long userId) {
        Optional<User> user = userRepository.findById(userId);

        return dtoConverter.toDto(user.get());
    }

}
