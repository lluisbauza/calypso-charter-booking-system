package com.lluisbauza.calypso.controller;

import com.lluisbauza.calypso.dto.ReservationRequest;
import com.lluisbauza.calypso.enums.SlotAvailability;
import com.lluisbauza.calypso.model.Reservation;
import com.lluisbauza.calypso.service.*;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.time.LocalDate;
import java.time.Month;
import java.time.Year;
import java.time.YearMonth;

@Controller
public class ReservationWebController {

    private final SlotService slotService;
    private final BoatService boatService;
    private final ClientService clientService;
    private final ReservationService reservationService;
    private final CalendarService calendarService;
    public ReservationWebController(
            SlotService slotService,
            BoatService boatService,
            ClientService clientService,
            ReservationService reservationService, CalendarService calendarService) {
        this.slotService = slotService;
        this.boatService = boatService;
        this.clientService = clientService;
        this.reservationService = reservationService;
        this.calendarService = calendarService;
    }

    @GetMapping("/dates")
    public String getAvailableDates(
            @RequestParam(required = false) Year year,
            @RequestParam(required = false) Month month,
            Model model) {

        LocalDate today = LocalDate.now();

        if (year == null) {
            year = Year.now();
        }

        if (month == null) {
            month = today.getMonth();
        }

        var current = YearMonth.of(year.getValue(), month);
        var previous = current.minusMonths(1);
        var next = current.plusMonths(1);

        var dates = slotService.findAllAvailableDates();
        var weeks = calendarService.generateMonth(
                current.getYear(),
                current.getMonthValue()
        );

        model.addAttribute("dates", dates);
        model.addAttribute("weeks", weeks);

        model.addAttribute("year", current.getYear());
        model.addAttribute("month", current.getMonth());

        model.addAttribute("previousYear", previous.getYear());
        model.addAttribute("previousMonth", previous.getMonth());

        model.addAttribute("nextYear", next.getYear());
        model.addAttribute("nextMonth", next.getMonth());

        return "dates";

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

    @GetMapping("/boats/fragment")
    public String getAvailableBoatsFragment(
            @RequestParam
            @DateTimeFormat(iso = DateTimeFormat.ISO.DATE)
            LocalDate date,
            Model model) {

        var boats = boatService.findBoatAvailableByDate(
                date,
                SlotAvailability.AVAILABLE
        );

        model.addAttribute("boats", boats);
        model.addAttribute("date", date);

        return "fragments/boats-fragment :: boats";
    }

    @GetMapping("/slots/fragment")
    public String getAvailableSlotsFragment(
            @RequestParam
            @DateTimeFormat(iso = DateTimeFormat.ISO.DATE)
            LocalDate date,
            @RequestParam Long boatId,
            Model model) {

        var slots = slotService.findAvailableSlotsByBoatIdAndDate(boatId, date);
        model.addAttribute("slots", slots);

        return "fragments/slots-fragment :: slots";
    }


}
