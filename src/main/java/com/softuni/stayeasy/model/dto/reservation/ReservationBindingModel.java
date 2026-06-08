package com.softuni.stayeasy.model.dto.reservation;

import java.time.LocalDate;

public class ReservationBindingModel {

    private LocalDate checkIn;
    private LocalDate checkOut;
    private int guests;

    public LocalDate getCheckIn() {
        return checkIn;
    }

    public void setCheckIn(LocalDate checkIn) {
        this.checkIn = checkIn;
    }

    public LocalDate getCheckOut() {
        return checkOut;
    }

    public void setCheckOut(LocalDate checkOut) {
        this.checkOut = checkOut;
    }

    public int getGuests() {
        return guests;
    }

    public void setGuests(int guests) {
        this.guests = guests;
    }
}
