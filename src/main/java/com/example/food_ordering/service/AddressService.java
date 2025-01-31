package com.example.food_ordering.service;

import com.example.food_ordering.dto.AddressDto;
import com.example.food_ordering.entities.Address;

import java.util.List;

public interface AddressService {
    AddressDto addAddress(AddressDto addressDto);
    List<AddressDto> getAddressesByUserId(long userId);
    AddressDto getAddressByUserId(long userId);

}
