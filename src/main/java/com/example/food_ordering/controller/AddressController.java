package com.example.food_ordering.controller;

import com.example.food_ordering.dto.AddressDto;
import com.example.food_ordering.service.AddressService;
import com.example.food_ordering.service.UserDetailsImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api")
public class AddressController {

    @Autowired
    private AddressService addressService;

    @PostMapping("/add-address")
    public ResponseEntity<?> addAddress(@RequestBody AddressDto addressDto){
        AddressDto addressDto1 = addressService.addAddress(addressDto);
        return ResponseEntity.ok(addressDto1);
    }
}
