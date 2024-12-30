package com.example.food_ordering.repository;

import com.example.food_ordering.entities.Address;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface AddressRepository extends JpaRepository<Address,Long> {
    @Query("select a from Address a where a.user.id=:id")
    List<Address> getAddressByUserId(@Param("id") long userId);
}
