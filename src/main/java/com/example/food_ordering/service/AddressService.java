package com.example.food_ordering.service;

import com.example.food_ordering.dto.AddressDto;

import java.util.List;

public interface AddressService {
    AddressDto addAddress(AddressDto addressDto);
    List<AddressDto> getAddressByUserId(long userId);
}
