package com.example.food_ordering.service;

import com.example.food_ordering.dto.BasketDto;
import com.example.food_ordering.entities.DiscountCode;

import java.util.List;

public interface DiscountCodeService {
    DiscountCode getDiscountCodeByCode(String code);
    List<DiscountCode> getAllDiscountCodes();
}
