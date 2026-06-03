package com.softuni.stayeasy.model.entity.user;

import com.softuni.stayeasy.model.entity.property.Property;
import com.softuni.stayeasy.model.entity.reservation.Reservation;
import com.softuni.stayeasy.model.entity.review.Review;
import jakarta.persistence.*;
import lombok.*;


import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Builder
@Getter
@Setter
@Entity
@AllArgsConstructor
@NoArgsConstructor
@Table(name = "users")
public class User {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;

    @Column(nullable = false, unique = true)
    private String username;

    @Column(nullable = false)
    private String password;

    @Column(nullable = false)
    private String email;

    private String firstName;

    private String lastName;

    private String phoneNumber;

    private String profilePicture;

    @Enumerated(EnumType.STRING)
    private UserRole role;

    private LocalDateTime createdOn;

    private LocalDateTime updatedOn;

    @OneToMany(fetch = FetchType.LAZY, mappedBy = "host")
    private List<Property> properties = new ArrayList<>();

    @OneToMany(fetch = FetchType.LAZY, mappedBy = "renter")
    private List<Reservation> reservations = new ArrayList<>();

    @OneToMany(fetch = FetchType.LAZY, mappedBy = "author")
    private List<Review> reviews = new ArrayList<>();
}
