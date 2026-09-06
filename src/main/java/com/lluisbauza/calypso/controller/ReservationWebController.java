package com.lluisbauza.calypso.controller;

import com.lluisbauza.calypso.dto.ReservationRequest;
import com.lluisbauza.calypso.enums.SlotAvailability;
import com.lluisbauza.calypso.model.Reservation;
import com.lluisbauza.calypso.service.BoatService;
import com.lluisbauza.calypso.service.ClientService;
import com.lluisbauza.calypso.service.ReservationService;
import com.lluisbauza.calypso.service.SlotService;
import jakarta.mail.MessagingException;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.time.LocalDate;

@Controller
public class ReservationWebController {

    private final SlotService slotService;
    private final BoatService boatService;
    private final ClientService clientService;
    private final ReservationService reservationService;
    public ReservationWebController(
            SlotService slotService,
            BoatService boatService,
            ClientService clientService,
            ReservationService reservationService) {
        this.slotService = slotService;
        this.boatService = boatService;
        this.clientService = clientService;
        this.reservationService = reservationService;
    }

    @GetMapping("/dates")
    public String getAvailableDates(Model model) {

        var dates = slotService.findAllAvailableDates();
        model.addAttribute("dates", dates);

        return "dates";

    }

    @GetMapping("/boats")
    public String getAvailableBoats(
            @RequestParam
            @DateTimeFormat(iso = DateTimeFormat.ISO.DATE)
            LocalDate date,
            Model model) {

        var boats =  boatService.findBoatAvailableByDate(date, SlotAvailability.AVAILABLE);
        model.addAttribute("boats", boats);

        return "boats";

    }

    @GetMapping("/slots")
    public String getAvailableSlotsByBoatId(
            @RequestParam Long boatId,
            Model model
    ) {

        var slots = slotService.findAvailableSlotsByBoatId(boatId);
        model.addAttribute("slots", slots);

        return "slots";
    }

    @GetMapping("/reservation")
    public String bookReservation(
            @RequestParam Long slotId,
            Model model
    ) {

        var slot = slotService.findById(slotId);
        model.addAttribute("slot", slot);

        return "email-form";
    }

    @GetMapping("/reservation/email")
    public String checkEmail(
            @RequestParam Long slotId,
            @RequestParam String email,
            Model model
    ) {

        ReservationRequest reservationRequest =
                clientService.getReservationRequestBySlotIdAndClientEmail(slotId, email);

        model.addAttribute("reservationRequest", reservationRequest);

        return "reservation-form";

    }

    @PostMapping("/reservation/email")
    public String createReservation(
            @ModelAttribute ReservationRequest reservationRequest,
            Model model
    ) {

        Reservation reservation = reservationService.createReservation(reservationRequest);
        model.addAttribute("reservation", reservation);

        return "reservation-confirmation";

    }

}
