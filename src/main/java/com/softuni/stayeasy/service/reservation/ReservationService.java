package com.softuni.stayeasy.service.reservation;

import com.softuni.stayeasy.model.entity.property.Property;
import com.softuni.stayeasy.model.entity.reservation.Reservation;
import com.softuni.stayeasy.model.entity.user.User;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface ReservationService {

    void createReservation(Reservation reservation);

    void cancelReservation(UUID id);

    void approveReservation(UUID id);

    void rejectReservation(UUID id);

    Optional<Reservation> findById(UUID id);

    List<Reservation> findAllByRenter(User renter);

    List<Reservation> findAllByProperty(Property property);

}
