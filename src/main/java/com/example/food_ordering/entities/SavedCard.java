package com.example.food_ordering.entities;

import com.example.food_ordering.util.CustomDateDeserializer;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDate;
import java.util.Date;
import java.util.List;

@Entity
@Getter
@Setter
public class SavedCard {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String cardImage;
    private String cardHolderName;
    private String cvv;
    private String maskedCardNumber;
    private String expiryDate;

    @OneToMany(mappedBy = "savedCard",cascade = CascadeType.ALL)
    private List<Payment> payments;

    @ManyToOne
    @JoinColumn(name = "user_id")
    private User user;

    public SavedCard() {
    }
}
