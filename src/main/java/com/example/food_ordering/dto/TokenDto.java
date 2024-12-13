package com.example.food_ordering.dto;

import com.example.food_ordering.converter.UserConverter;
import com.example.food_ordering.service.UserDetailsImpl;
import com.example.food_ordering.util.DTOConverter;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class TokenDto {

    private String accessToken;
    private String refreshToken;

    public TokenDto() {
    }

    public TokenDto(String accessToken, String refreshToken) {
        this.accessToken = accessToken;
        this.refreshToken = refreshToken;
    }
}
