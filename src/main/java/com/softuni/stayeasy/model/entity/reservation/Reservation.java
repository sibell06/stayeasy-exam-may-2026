package com.softuni.stayeasy.model.entity.reservation;

import com.softuni.stayeasy.model.entity.property.Property;
import com.softuni.stayeasy.model.entity.user.User;
import jakarta.persistence.*;
import lombok.*;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.UUID;

@Builder
@Getter
@Setter
@Entity
@AllArgsConstructor
@NoArgsConstructor
@Table(name = "reservations")
public class Reservation {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;

    @Column(nullable = false)
    private LocalDate checkIn;

    @Column(nullable = false)
    private LocalDate checkOut;

    @Column(nullable = false)
    private BigDecimal totalPrice;

    private int guests;

    @Enumerated(EnumType.STRING)
    private ReservationStatus status;

    private LocalDateTime created;

    @ManyToOne
    @JoinColumn(name = "renter_id", nullable = false)
    private User renter;

    @ManyToOne
    @JoinColumn(name = "property_id", nullable = false)
    private Property property;
}
