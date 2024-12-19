package com.example.food_ordering.controller;

import com.example.food_ordering.dto.TokenDto;
import com.example.food_ordering.dto.UserDto;
import com.example.food_ordering.entities.User;
import com.example.food_ordering.response.ApiResponse;
import com.example.food_ordering.service.AuthService;
import com.example.food_ordering.service.FileStorageService;
import com.example.food_ordering.service.JwtService;
import com.example.food_ordering.service.UserDetailsImpl;
import com.example.food_ordering.util.JWTProvider;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.JwtException;
import io.jsonwebtoken.Jwts;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.Resource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/api/auth")
public class AuthController {

    @Value("${jwt_token}")
    private String secretKey;

    @Autowired
    private AuthenticationManager authenticationManager;
    @Autowired
    private AuthService authService;
    @Autowired
    private JWTProvider jwtProvider;
    @Autowired
    private FileStorageService fileStorageService;


    @PostMapping("/login")
    public ResponseEntity<?> login(@Valid @RequestBody UserDto userDto){

        Authentication authentication = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(userDto.getEmail(), userDto.getPassword()));

        SecurityContextHolder.getContext().setAuthentication(authentication);
        UserDetailsImpl userDetails = (UserDetailsImpl) authentication.getPrincipal();

        if (!userDetails.user.isEnabled()) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN)
                    .body("Please, firstly verify your email address");
        }

        String jwt = jwtProvider.generateToken(userDetails);
        String refreshToken = jwtProvider.generateRefreshToken(userDetails);

        return ResponseEntity.ok(new TokenDto(jwt, refreshToken));
    }


    @PostMapping("/register")
    public ResponseEntity<UserDto> register(@RequestBody UserDto userDto,
                                            @RequestBody MultipartFile profilePicture){
        authService.register(userDto,profilePicture);
        System.out.println("User DTO: " + userDto);
        return ResponseEntity.ok(userDto);
    }


    @GetMapping("/verify")
    public ResponseEntity<?> verifyUser(@RequestParam String token){
        boolean isVerified = authService.verifyUser(token);
        if (isVerified){
            return ResponseEntity.ok("User verified successfully");
        }else{
            return ResponseEntity.badRequest().body("Verification token is invalid or expired");
        }
    }

    @GetMapping("/uploads/{fileType}/{filename:.+}")
    public ResponseEntity<Resource> getImage(@PathVariable String filename,
                                             @PathVariable String fileType) {

        Resource file = fileStorageService.loadFile(filename,fileType);

        return ResponseEntity.ok()
                .header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=\"" + file.getFilename() + "\"").body(file);
    }
    @PostMapping("/refresh-token")
    public ResponseEntity<Map<String,String>> refreshToken(HttpServletRequest request){
        String refreshToken = request.getHeader("Authorization").substring(7);

        try {
            Claims claims = Jwts.parser()
                    .setSigningKey(secretKey)
                    .parseClaimsJws(refreshToken)
                    .getBody();

            String username = claims.getSubject();
            UserDetailsImpl userDetails = authService.findByUsername(username);
            String newAccessToken = jwtProvider.generateToken(userDetails);

            Map<String,String> tokens = new HashMap<String,String>();
            tokens.put("accessToken",newAccessToken);
            return ResponseEntity.ok(tokens);
        }catch (JwtException e){
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
        }
    }

}


























