package com.example.food_ordering.controller;

import com.example.food_ordering.dto.TokenDto;
import com.example.food_ordering.dto.UserDto;
import com.example.food_ordering.service.AuthService;
import com.example.food_ordering.service.UserDetailsImpl;
import com.example.food_ordering.util.JWTProvider;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api")
public class AuthController {

    @Autowired
    private AuthenticationManager authenticationManager;
    @Autowired
    private AuthService authService;
    @Autowired
    private JWTProvider jwtProvider;

    @PostMapping("/login")
    public ResponseEntity<?> login(@RequestBody UserDto userDto){

        Authentication authentication = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(userDto.getEmail(), userDto.getPassword()));

        SecurityContextHolder.getContext().setAuthentication(authentication);
        UserDetailsImpl userDetails = (UserDetailsImpl) authentication.getPrincipal();

        if (!userDetails.getUser().isEnabled()) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN).body("Please, firstly verify your email address");
        }

        String jwt = jwtProvider.generateToken(authentication);

        return ResponseEntity.ok(new TokenDto(jwt));
    }

    @PostMapping("/register")
    public ResponseEntity<?> register(@RequestBody UserDto userDto){
        authService.register(userDto);
        return ResponseEntity.ok(userDto);
    }


    @PostMapping("/verify")
    public ResponseEntity<?> verifyUser(@RequestParam String token){
        boolean isVerified = authService.verifyUser(token);
        if (isVerified){
            return ResponseEntity.ok("User verified successfully");
        }else{
            return ResponseEntity.badRequest().body("Verification token is invalid or expired");
        }
    }

}


























