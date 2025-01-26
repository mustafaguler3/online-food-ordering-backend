package com.example.food_ordering.repository;

import com.example.food_ordering.entities.DiscountCode;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface DiscountCodeRepository extends JpaRepository<DiscountCode,Long> {
    Optional<DiscountCode> findByCode(String code);
}
