package com.softuni.stayeasy.repository.reservation;

import com.softuni.stayeasy.model.entity.property.Property;
import com.softuni.stayeasy.model.entity.reservation.Reservation;
import com.softuni.stayeasy.model.entity.reservation.ReservationStatus;
import com.softuni.stayeasy.model.entity.user.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.UUID;

@Repository
public interface ReservationRepository extends JpaRepository<Reservation, UUID> {

    List<Reservation> findAllByRenter(User renter);

    List<Reservation> findAllByProperty(Property property);

    List<Reservation> findAllByPropertyAndStatus(Property property, ReservationStatus status);

}
