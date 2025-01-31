package com.example.food_ordering.controller;

import com.example.food_ordering.dto.AddressDto;
import com.example.food_ordering.service.AddressService;
import com.example.food_ordering.service.UserDetailsImpl;
import com.example.food_ordering.util.CurrentUserProvider;
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
    @Autowired
    private CurrentUserProvider currentUserProvider;

    @PostMapping("/add-address")
    public ResponseEntity<?> addAddress(@RequestBody AddressDto addressDto){
        AddressDto addressDto1 = addressService.addAddress(addressDto);
        return ResponseEntity.ok(addressDto1);
    }

    @GetMapping("/address/get-addresses")
    public ResponseEntity<?> getAddress(){
        UserDetailsImpl userDetails = currentUserProvider.getCurrentUserDetails();

        List<AddressDto> addressDto = addressService.getAddressesByUserId(userDetails.user.getId());

        return ResponseEntity.ok(addressDto);
    }

    @GetMapping("/address/get-address")
    public ResponseEntity<?> getAddressByAddressId(@RequestParam long addressId){
        UserDetailsImpl userDetails = currentUserProvider.getCurrentUserDetails();

        List<AddressDto> addressDto = addressService.getAddressesByUserId(userDetails.user.getId());

        for(AddressDto addressDto1 : addressDto) {
            if(addressDto1.getId() == addressId) {
                return ResponseEntity.ok(addressDto1);
            }
        }

        return ResponseEntity.badRequest().body("Address not found");
    }


}
