package com.example.food_ordering.service.impl;

import com.example.food_ordering.entities.DiscountCode;
import com.example.food_ordering.repository.DiscountCodeRepository;
import com.example.food_ordering.service.DiscountCodeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class DiscountServiceImpl implements DiscountCodeService {

    @Autowired
    private DiscountCodeRepository discountCodeRepository;

    @Override
    public DiscountCode getDiscountCodeByCode(String code) {
        return discountCodeRepository.findByCode(code).orElse(null);
    }

    @Override
    public List<DiscountCode> getAllDiscountCodes() {
        return discountCodeRepository.findAll();
    }
}
