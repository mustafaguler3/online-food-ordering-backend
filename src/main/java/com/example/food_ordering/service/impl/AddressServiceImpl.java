package com.example.food_ordering.service.impl;

import com.example.food_ordering.dto.AddressDto;
import com.example.food_ordering.entities.Address;
import com.example.food_ordering.entities.User;
import com.example.food_ordering.repository.AddressRepository;
import com.example.food_ordering.repository.UserRepository;
import com.example.food_ordering.service.AddressService;
import com.example.food_ordering.service.UserDetailsImpl;
import com.example.food_ordering.util.DTOConverter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.List;
import java.util.Optional;

@Service
public class AddressServiceImpl implements AddressService {

    @Autowired
    private AddressRepository addressRepository;
    @Autowired
    private DTOConverter dtoConverter;
    @Autowired
    private UserRepository userRepository;

    @Override
    public AddressDto addAddress(AddressDto addressDto) {
        Address address = new Address();
        address.setAddressLine1(addressDto.getAddressLine1());
        address.setAddressLine2(addressDto.getAddressLine2());
        address.setCreatedDate(new Date());
        address.setCity(addressDto.getCity());
        address.setCountry(addressDto.getCountry());
        address.setFirstName(addressDto.getFirstName());
        address.setLastName(addressDto.getLastName());
        address.setPhone(addressDto.getPhone());
        address.setZipCode(addressDto.getZipCode());
        address.setType(addressDto.getType());
        address.setState(addressDto.getState());

        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        UserDetailsImpl userDetails = (UserDetailsImpl) authentication.getPrincipal();
        long userId =  userDetails.user.getId();
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new UsernameNotFoundException("User not found" + userId));

        address.setUser(user);
        addressRepository.save(address);
        return dtoConverter.toAddressDto(address);
    }

    @Override
    public List<AddressDto> getAddressesByUserId(long userId) {
        Optional<User> user = userRepository.findById(userId);
        if (user.isEmpty()){
            throw new UsernameNotFoundException("User not found" + userId);
        }
        List<Address> addresses =
                addressRepository
                        .getAddressesByUserId(userId)
                        .stream().toList();

        if (addresses.isEmpty()){
            return null;
        }

        List<AddressDto> addressDtos =
                addresses.stream()
                        .map(address -> dtoConverter.toAddressDto(address)).toList();

        return addressDtos;
    }

    @Override
    public AddressDto getAddressByUserId(long userId) {
        Address address = addressRepository.getAddressByUserId(userId);
        return dtoConverter.toAddressDto(address);
    }


}





















