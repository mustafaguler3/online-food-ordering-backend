package com.example.food_ordering.controller;

import com.example.food_ordering.dto.AddressDto;
import com.example.food_ordering.dto.UserDto;
import com.example.food_ordering.entities.Address;
import com.example.food_ordering.service.AddressService;
import com.example.food_ordering.service.UserDetailsImpl;
import com.example.food_ordering.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api/user")
public class UserController {
    @Autowired
    private UserService userService;
    @Autowired
    private AddressService addressService;

    @GetMapping
    public ResponseEntity<UserDto> getUserInfo(@AuthenticationPrincipal UserDetailsImpl userDetails) {
        UserDto userDto = userDetails.getUserDto();

        return ResponseEntity.ok(userDto);
    }

    @GetMapping("/account/profile")
    public ResponseEntity<?> getUserProfile(){
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        UserDetailsImpl userDetails = (UserDetailsImpl) authentication.getPrincipal();
        UserDto userDto = userService.getUserProfile(userDetails.user.getId());

        return ResponseEntity.ok(userDto);
    }

    @GetMapping("/account/saved-address")
    public ResponseEntity<?> getSavedAddress(@AuthenticationPrincipal UserDetailsImpl userDetails) {
        if (userDetails == null) {
            return ResponseEntity.status(401).body("Unauthorized");
        }
        long userId = userDetails.user.getId();

        List<AddressDto> addresses = addressService.getAddressByUserId(userId);

        return ResponseEntity.ok(addresses);
    }

}
