package com.example.food_ordering.service.impl;

import com.example.food_ordering.dto.TokenDto;
import com.example.food_ordering.dto.UserDto;
import com.example.food_ordering.entities.Role;
import com.example.food_ordering.entities.User;
import com.example.food_ordering.entities.VerificationToken;
import com.example.food_ordering.repository.RoleRepository;
import com.example.food_ordering.repository.UserRepository;
import com.example.food_ordering.repository.VerificationTokenRepository;
import com.example.food_ordering.service.AuthService;
import com.example.food_ordering.service.EmailService;
import com.example.food_ordering.service.FileStorageService;
import com.example.food_ordering.service.UserDetailsImpl;
import com.example.food_ordering.util.DTOConverter;
import com.example.food_ordering.util.JWTProvider;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.util.Collections;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Service
public class AuthServiceImpl implements AuthService {

    @Autowired
    private UserRepository userRepository;
    @Autowired
    private PasswordEncoder passwordEncoder;
    @Autowired
    private RoleRepository roleRepository;
    @Autowired
    private JWTProvider jwtProvider;
    @Autowired
    private AuthenticationManager authenticationManager;
    @Autowired
    private DTOConverter dtoConverter;
    @Autowired
    private EmailService emailService;
    @Autowired
    private VerificationTokenRepository verificationTokenRepository;
    @Autowired
    private FileStorageService fileStorageService;


    @Override
    public TokenDto login(UserDto userDto) {
        Authentication authentication = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(userDto.getEmail(), userDto.getPassword()));

        SecurityContextHolder.getContext().setAuthentication(authentication);
        UserDetailsImpl userDetails = (UserDetailsImpl) authentication.getPrincipal();

        String accessToken = jwtProvider.generateToken(userDetails);
        String refreshToken = jwtProvider.generateRefreshToken(userDetails);

        return new TokenDto(accessToken,refreshToken);
    }

    @Override
    public void register(UserDto userDto,MultipartFile profilePicture) {
        User user = new User();
        user.setEmail(userDto.getEmail());
        user.setUsername(userDto.getUsername());
        user.setPassword(passwordEncoder.encode(userDto.getPassword()));
        user.setFirstName(userDto.getFirstName());
        user.setProfileImage(userDto.getProfileImage());
        user.setLastName(userDto.getLastName());
        user.setPhoneNumber(userDto.getPhoneNumber());
        user.setEnabled(false);



        if (findByEmail(userDto.getEmail()) != null){
            throw new RuntimeException("Email already taken");
        }

        Role role = roleRepository.findByRoleName("ROLE_USER");

        if (role == null){
            Role newRole = new Role();
            newRole.setRoleName("ROLE_USER");
            roleRepository.save(newRole);
        }
        user.setRoles(Collections.singleton(role));

        //MultipartFile profilePicture = userDto.getProfileImage();

        if (profilePicture != null && !profilePicture.isEmpty()){
            try {
                String fileName = fileStorageService.storeFile(profilePicture,"users");
                user.setProfileImage(fileName);
            }catch (Exception e){
                e.printStackTrace();
                throw new RuntimeException("Failed to upload profile picture");
            }
        }

        userRepository.save(user);

        String token = UUID.randomUUID().toString();
        VerificationToken verificationToken = new VerificationToken();
        verificationToken.setToken(token);
        verificationToken.setUser(user);
        verificationTokenRepository.save(verificationToken);
        emailService.sendVerificationEmail(userDto,token);
    }

    @Override
    public UserDetailsImpl findByUsername(String username) {
        Optional<User> user = userRepository.findByUsername(username);

        return user.map(UserDetailsImpl::new).orElseThrow(() ->
                new UsernameNotFoundException("User not found with username: " + username));
    }


    @Override
    public UserDto findByEmail(String email) {
        Optional<User> user = userRepository.findByEmail(email);

        if (user.isEmpty()){
            return null;
        }

        return dtoConverter.toDto(user.get());
    }

    @Override
    public boolean verifyUser(String token) {
        VerificationToken verifier = verificationTokenRepository.findByToken(token);

        if (verifier == null){
            throw new RuntimeException("Invalid token");
        }
        User user = verifier.getUser();
        user.setEnabled(true);
        userRepository.save(user);
        verificationTokenRepository.delete(verifier);

        return true;
    }
}



























