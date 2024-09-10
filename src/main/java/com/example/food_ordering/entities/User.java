package com.example.food_ordering.entities;

import jakarta.persistence.*;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

import java.util.Set;

@Entity
@Setter
@Getter
@Builder
public class User {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;
    private String username;
    private String email;
    private String password;
    private String phoneNumber;
    private String profileImage;
    private String firstName;
    private String lastName;
    private boolean isEnabled;

    @OneToOne(mappedBy = "user")
    private VerificationToken verificationToken;

    @ManyToMany(fetch = FetchType.EAGER)
    @JoinTable(name = "user_roles",
            joinColumns = @JoinColumn(name = "user_id"),
            inverseJoinColumns = @JoinColumn(name = "role_id"))
    private Set<Role> roles;


    public User() {
    }

    public User(long id, String username, String email, String password, String phoneNumber, String profileImage, String firstName, String lastName, boolean isEnabled, VerificationToken verificationToken, Set<Role> roles) {
        this.id = id;
        this.username = username;
        this.email = email;
        this.password = password;
        this.phoneNumber = phoneNumber;
        this.profileImage = profileImage;
        this.firstName = firstName;
        this.lastName = lastName;
        this.isEnabled = isEnabled;
        this.verificationToken = verificationToken;
        this.roles = roles;
    }
}























