package com.softuni.stayeasy.model.entity.property;

import com.softuni.stayeasy.model.entity.reservation.Reservation;
import com.softuni.stayeasy.model.entity.review.Review;
import com.softuni.stayeasy.model.entity.user.User;
import jakarta.persistence.*;
import lombok.*;

import java.math.BigDecimal;
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
@Table(name = "properties")
public class Property {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;

    @Column(nullable = false)
    private String title;

    @Column(nullable = false, length = 2000)
    private String description;

    @Column(nullable = false)
    private String location;

    @Column(nullable = false)
    private BigDecimal pricePerNight;

    private int maxGuest;

    private int bedrooms;

    private int bathrooms;

    private String imageUrl;

    @Enumerated(EnumType.STRING)
    private PropertyType type;

    private boolean available;

    private LocalDateTime createdOn;

    private LocalDateTime updatedOn;

    @ManyToOne
    @JoinColumn(name = "host_id", nullable = false)
    private User host;

    @OneToMany(fetch = FetchType.LAZY, mappedBy = "property")
    private List<Reservation> reservations = new ArrayList<>();

    @OneToMany(fetch = FetchType.LAZY, mappedBy = "property")
    private List<Review> reviews = new ArrayList<>();
}
