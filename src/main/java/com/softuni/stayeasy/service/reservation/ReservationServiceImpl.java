package com.softuni.stayeasy.service.reservation;

import com.softuni.stayeasy.model.entity.property.Property;
import com.softuni.stayeasy.model.entity.reservation.Reservation;
import com.softuni.stayeasy.model.entity.reservation.ReservationStatus;
import com.softuni.stayeasy.model.entity.user.User;
import com.softuni.stayeasy.repository.reservation.ReservationRepository;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Service
public class ReservationServiceImpl implements ReservationService {

    private final ReservationRepository reservationRepository;

    public ReservationServiceImpl(ReservationRepository reservationRepository, ReservationRepository reservationRepository1) {
        this.reservationRepository = reservationRepository1;
    }

    @Override
    public void createReservation(Reservation reservation) {
        reservation.setStatus(ReservationStatus.PENDING);
        reservation.setCreated(LocalDateTime.now());
        reservationRepository.save(reservation);
    }

    @Override
    public void cancelReservation(UUID id) {
        reservationRepository.findById(id).ifPresent(reservation -> {
            reservation.setStatus(ReservationStatus.CANCELLED);
            reservationRepository.save(reservation);
        });
    }

    @Override
    public void approveReservation(UUID id) {
        reservationRepository.findById(id).ifPresent(reservation -> {
            reservation.setStatus(ReservationStatus.APPROVED);
            reservationRepository.save(reservation);
        });
    }

    @Override
    public void rejectReservation(UUID id) {
        reservationRepository.findById(id).ifPresent(reservation -> {
            reservation.setStatus(ReservationStatus.REJECTED);
            reservationRepository.save(reservation);
        });
    }

    @Override
    public Optional<Reservation> findById(UUID id) {
        return reservationRepository.findById(id);
    }

    @Override
    public List<Reservation> findAllByRenter(User renter) {
        return reservationRepository.findAllByRenter(renter);
    }

    @Override
    public List<Reservation> findAllByProperty(Property property) {
        return reservationRepository.findAllByProperty(property);
    }
}
