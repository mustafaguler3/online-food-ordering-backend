package com.example.food_ordering.entities;

import com.example.food_ordering.dto.BasketItemDto;
import com.example.food_ordering.dto.UserDto;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Entity
@Getter
@Setter
public class Basket {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;
    @OneToMany(mappedBy = "basket",fetch = FetchType.LAZY,cascade = CascadeType.ALL)
    private List<BasketItem> basketItems = new ArrayList<>();
    private double totalPrice;
    @OneToOne
    @JoinColumn(name = "user_id")
    private User user;
    private double discount;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;

    public Basket() {
    }

    public Basket(int id, User user) {
        this.id = id;
        this.user = user;
        this.createdAt = LocalDateTime.now();
        this.updatedAt = LocalDateTime.now();
    }


}

















