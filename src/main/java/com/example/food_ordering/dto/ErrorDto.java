package com.example.food_ordering.dto;

import lombok.Data;

import java.time.LocalDateTime;

@Data
public class ErrorDto {
    private String message;
    private int statusCode;
    private LocalDateTime timestamp;
    private String path;
    private String error;
    private String trace;
}
