package com.example.food_ordering.repository;

import com.example.food_ordering.entities.SavedCard;
import com.example.food_ordering.entities.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface SavedCardRepository extends JpaRepository<SavedCard,Long> {
    List<SavedCard> findSavedCardsByUser(User user);
}
