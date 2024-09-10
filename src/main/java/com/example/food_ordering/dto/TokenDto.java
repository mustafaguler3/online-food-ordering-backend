package com.example.food_ordering.dto;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class TokenDto {

    private String token;

    public TokenDto() {
    }

    public TokenDto(String token) {
        this.token = token;
    }
}
