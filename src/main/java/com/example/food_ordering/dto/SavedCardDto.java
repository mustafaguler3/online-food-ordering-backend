package com.example.food_ordering.dto;

import com.example.food_ordering.util.CustomDateDeserializer;
import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDate;
import java.util.Date;

@Getter
@Setter
public class SavedCardDto {

    private Long id;
    @NotNull(message = "Card Holder Name is required")
    private String cardHolderName;
    @NotNull(message = "Card Number is required")
    private String cardNumber;
    @NotNull(message = "Cvv is required")
    private String cvv;
    @NotNull(message = "Expiration Date is required")
    @JsonFormat(pattern = "MM/yy")
    private String expiryDate;

    private Long userId;
}
