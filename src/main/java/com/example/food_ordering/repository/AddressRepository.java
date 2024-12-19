package com.example.food_ordering.repository;

import com.example.food_ordering.entities.Address;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface AddressRepository extends JpaRepository<Address,Long> {
    Optional<Address> getAddressByUserId(long userId);
}
