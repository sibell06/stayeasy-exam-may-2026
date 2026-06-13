package com.softuni.stayeasy.web;

import com.softuni.stayeasy.model.dto.reservation.ReservationBindingModel;
import com.softuni.stayeasy.model.entity.property.Property;
import com.softuni.stayeasy.model.entity.reservation.Reservation;
import com.softuni.stayeasy.model.entity.user.User;
import com.softuni.stayeasy.service.property.PropertyService;
import com.softuni.stayeasy.service.reservation.ReservationService;
import com.softuni.stayeasy.service.user.UserService;
import jakarta.servlet.http.HttpSession;
import jakarta.validation.Valid;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.temporal.ChronoUnit;
import java.util.Optional;
import java.util.UUID;

@Controller
@RequestMapping("/reservations")
public class ReservationController {

    private final ReservationService reservationService;
    private final PropertyService propertyService;
    private final UserService userService;

    public ReservationController(ReservationService reservationService,
                                 PropertyService propertyService,
                                 UserService userService) {
        this.reservationService = reservationService;
        this.propertyService = propertyService;
        this.userService = userService;
    }

    // --- CREATE ---

    @GetMapping("/create/{propertyId}")
    public String createPage(@PathVariable UUID propertyId,
                             Model model,
                             HttpSession session) {
        if (session.getAttribute("userId") == null) {
            return "redirect:/auth/login";
        }

        Optional<Property> propertyOpt = propertyService.findById(propertyId);
        if (propertyOpt.isEmpty()) {
            return "redirect:/properties";
        }
        model.addAttribute("property", propertyOpt.get());
        model.addAttribute("reservationData", new ReservationBindingModel());
        return "reservation/create";
    }

    @PostMapping("/create/{propertyId}")
    public String create(@PathVariable UUID propertyId,
                         @Valid @ModelAttribute("reservationData") ReservationBindingModel reservationData,
                         BindingResult bindingResult,
                         Model model,
                         HttpSession session) {

        if (session.getAttribute("userId") == null) {
            return "redirect:/auth/login";
        }

        Optional<Property> propertyOpt = propertyService.findById(propertyId);
        if (propertyOpt.isEmpty()) {
            return "redirect:/properties";
        }

        Property property = propertyOpt.get();

        if (bindingResult.hasErrors()) {
            model.addAttribute("property", property);
            return "reservation/create";
        }

        // Validate check-in is not in the past
        if (reservationData.getCheckIn().isBefore(LocalDate.now())) {
            model.addAttribute("property", property);
            model.addAttribute("pastDateError", true);
            return "reservation/create";
        }

        // Validate checkout is at least 1 night after checkin
        if (!reservationData.getCheckOut().isAfter(reservationData.getCheckIn())) {
            model.addAttribute("property", property);
            model.addAttribute("dateError", true);
            return "reservation/create";
        }

        // Validate guests against property max
        if (reservationData.getGuests() > property.getMaxGuest()) {
            model.addAttribute("property", property);
            model.addAttribute("guestError", true);
            return "reservation/create";
        }

        UUID userId = UUID.fromString((String) session.getAttribute("userId"));
        Optional<User> renterOpt = userService.findById(userId);
        if (renterOpt.isEmpty()) {
            return "redirect:/auth/login";
        }

        long nights = ChronoUnit.DAYS.between(reservationData.getCheckIn(), reservationData.getCheckOut());
        BigDecimal totalPrice = property.getPricePerNight().multiply(BigDecimal.valueOf(nights));

        Reservation reservation = Reservation.builder()
                .checkIn(reservationData.getCheckIn())
                .checkOut(reservationData.getCheckOut())
                .guests(reservationData.getGuests())
                .totalPrice(totalPrice)
                .renter(renterOpt.get())
                .property(property)
                .build();

        reservationService.createReservation(reservation);
        return "redirect:/reservations/my";
    }

    // --- MY RESERVATIONS (renter) ---

    @GetMapping("/my")
    public String myReservation(Model model, HttpSession session) {
        if (session.getAttribute("userId") == null) {
            return "redirect:/auth/login";
        }
        UUID userId = UUID.fromString((String) session.getAttribute("userId"));
        Optional<User> renterOpt = userService.findById(userId);
        if (renterOpt.isEmpty()) {
            return "redirect:/auth/login";
        }
        model.addAttribute("reservations", reservationService.findAllByRenter(renterOpt.get()));
        return "reservation/my-reservations";
    }

    // --- CANCEL (renter) ---

    @PostMapping("/{id}/cancel")
    public String cancel(@PathVariable UUID id, HttpSession session) {
        if (session.getAttribute("userId") == null) {
            return "redirect:/auth/login";
        }
        Optional<Reservation> reservationOpt = reservationService.findById(id);
        if (reservationOpt.isEmpty()) {
            return "redirect:/reservations/my";
        }
        UUID userId = UUID.fromString((String) session.getAttribute("userId"));

        if (!reservationOpt.get().getRenter().getId().equals(userId)) {
            return "redirect:/reservations/my";
        }
        reservationService.cancelReservation(id);
        return "redirect:/reservations/my";
    }

    // --- HOST DASHBOARD ---

    @GetMapping("/host")
    public String hostDashboard(Model model, HttpSession session) {
        if (session.getAttribute("userId") == null) {
            return "redirect:/auth/login";
        }

        UUID userId = UUID.fromString((String) session.getAttribute("userId"));
        Optional<User> hostOpt = userService.findById(userId);
        if (hostOpt.isEmpty()) {
            return "redirect:/auth/login";
        }

        var hostProperties = propertyService.findAllByHost(hostOpt.get());
        var allReservations = hostProperties.stream()
                .flatMap(p -> reservationService.findAllByProperty(p).stream())
                .toList();
        model.addAttribute("reservations", allReservations);
        model.addAttribute("properties", hostProperties);
        return "reservation/host-dashboard";
    }

    // --- APPROVE / REJECT (host) ---

    @PostMapping("/{id}/approve")
    public String approve(@PathVariable UUID id, HttpSession session) {
        if (session.getAttribute("userId") == null) {
            return "redirect:/auth/login";
        }
        Optional<Reservation> reservationOpt = reservationService.findById(id);
        if (reservationOpt.isEmpty()) {
            return "redirect:/reservations/host";
        }
        UUID userId = UUID.fromString((String) session.getAttribute("userId"));

        if (!reservationOpt.get().getProperty().getHost().getId().equals(userId)) {
            return "redirect:/reservations/host";
        }
        reservationService.approveReservation(id);
        return "redirect:/reservations/host";
    }

    @PostMapping("/{id}/reject")
    public String reject(@PathVariable UUID id, HttpSession session) {
        if (session.getAttribute("userId") == null) {
            return "redirect:/auth/login";
        }
        Optional<Reservation> reservationOpt = reservationService.findById(id);
        if (reservationOpt.isEmpty()) {
            return "redirect:/reservations/host";
        }
        UUID userId = UUID.fromString((String) session.getAttribute("userId"));

        if (!reservationOpt.get().getProperty().getHost().getId().equals(userId)) {
            return "redirect:/reservations/host";
        }
        reservationService.rejectReservation(id);
        return "redirect:/reservations/host";
    }
}