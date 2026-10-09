package com.lluisbauza.calypso.controller;

import com.lluisbauza.calypso.dto.ReservationEditRequest;
import com.lluisbauza.calypso.dto.ReservationSearchRequest;
import com.lluisbauza.calypso.dto.ReservationRequest;
import com.lluisbauza.calypso.enums.SlotAvailability;
import com.lluisbauza.calypso.model.Boat;
import com.lluisbauza.calypso.model.Reservation;
import com.lluisbauza.calypso.service.*;
import jakarta.validation.Valid;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.time.*;

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
            @RequestParam(required = false) Long reservationId,
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

        if (reservationId != null) {
            model.addAttribute("reservationId", reservationId);

            String reservationCode = reservationService.getReservationCode(reservationId);
            model.addAttribute("reservationCode", reservationCode);

            LocalDate reservedDate = reservationService.getReservationDate(reservationId);
            model.addAttribute("reservedDate", reservedDate);
            dates.add(reservedDate);
        }

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

    @GetMapping("/boats/fragment")
    public String getAvailableBoatsFragment(
            @RequestParam
            @DateTimeFormat(iso = DateTimeFormat.ISO.DATE)
            LocalDate date,
            @RequestParam(required = false) Long reservationId,
            Model model) {

        var boats = boatService.findBoatAvailableByDate(
                date,
                SlotAvailability.AVAILABLE
        );

        if (reservationId != null) {
            model.addAttribute("reservationId", reservationId);
            String reservationCode = reservationService.getReservationCode(reservationId);
            model.addAttribute("reservationCode", reservationCode);

            Boat reservedBoat = reservationService.getBoatByReservationId(reservationId);
            model.addAttribute("reservedBoat", reservedBoat);

            if (date.equals(reservationService.getReservationDate(reservationId))) {
                boats.add(reservedBoat);
            }
        }

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
            @RequestParam Integer pax,
            @RequestParam(required = false) Long reservationId,
            Model model
    ) {
        var slots = slotService.findAvailableSlotsByBoatIdAndDate(boatId, date);

        if (reservationId != null) {
            model.addAttribute("reservationId", reservationId);

            String reservationCode =
                    reservationService.getReservationCode(reservationId);

            model.addAttribute("reservationCode", reservationCode);

            var reservedSlot =
                    reservationService.getSlotByReservationId(reservationId);

            model.addAttribute("reservedSlot", reservedSlot);

            if (date.equals(reservationService.getReservationDate(reservationId))
                    && boatId.equals(reservedSlot.getTrip().getBoat().getId())) {
                slots.add(reservedSlot);
            }
        }

        model.addAttribute("slots", slots);
        model.addAttribute("pax", pax);

        return "fragments/slots-fragment :: slots";
    }

    @GetMapping("/reservation")
    public String bookReservation(
            @RequestParam Long slotId,
            @RequestParam Integer pax,
            Model model
    ) {
        var slot = slotService.findById(slotId);

        if (slot == null) {
            throw new IllegalArgumentException("Slot not found");
        }

        model.addAttribute("slot", slot);
        model.addAttribute("pax", pax);

        return "fragments/email-form";
    }

    @GetMapping("/reservation/email")
    public String checkEmail(
            @RequestParam Long slotId,
            @RequestParam Integer pax,
            @RequestParam String email,
            Model model
    ) {
        ReservationRequest reservationRequest =
                clientService.getReservationRequestBySlotIdAndClientEmail(
                        slotId,
                        email
                );

        reservationRequest.setPax(pax);

        model.addAttribute("reservationRequest", reservationRequest);

        return "reservation-form";
    }

    @PostMapping("/reservation/email")
    public String createReservation(
            @Valid @ModelAttribute ReservationRequest reservationRequest,
            BindingResult bindingResult,
            Model model
    ) {

        if (bindingResult.hasErrors()) {
            return "reservation-form";
        }

        Reservation reservation = reservationService.createReservation(reservationRequest);
        model.addAttribute("reservation", reservation);

        return "reservation-confirmation";

    }

    @GetMapping("/edit")
    public String editReservation(Model model) {
        return "edit-form";
    }

    @PostMapping("/edit/search")
    public String findReservation(
            @Valid @ModelAttribute ReservationSearchRequest reservationSearchRequest,
            BindingResult bindingResult,
            Model model
    ) {

        if (bindingResult.hasErrors()) {
            return "edit-form";
        }

        Reservation reservation = reservationService.getReservationByCodeAndEmailIfConfirmed(reservationSearchRequest.getReservationCode(), reservationSearchRequest.getEmail());

        if (reservation == null) {
            model.addAttribute("notFound", "Reservation not found.");
            return "fragments/edit-options";
        }

        model.addAttribute("reservationId", reservation.getId());

        return "fragments/edit-options";

    }

    @GetMapping("/edit/options/date")
    public String getModifyOptions(
            @RequestParam Long reservationId,
            Model model, RedirectAttributes redirectAttributes) {

        redirectAttributes.addAttribute("reservationId", reservationId);

        return "redirect:/dates";
    }

    @GetMapping("/edit/date/confirm")
    public String confirmDateChange(
            @RequestParam Long reservationId,
            @RequestParam Long newSlotId,
            @RequestParam Integer newPax,
            Model model
    ) {
        model.addAttribute("reservationId", reservationId);
        model.addAttribute("newSlotId", newSlotId);
        model.addAttribute("oldReservation", reservationService.getReservationBasicInfo(reservationId));
        model.addAttribute("newReservation", reservationService.getNewReservationInfo(reservationId, newSlotId));
        model.addAttribute("newPax", newPax);
        model.addAttribute(
                "oldPax",
                reservationService.getReservationPax(reservationId)
        );

        return "fragments/date-change-confirmation :: confirmation";
    }

    @PostMapping("/edit/date/success")
    public String updateDateChange(
            @RequestParam Long reservationId,
            @RequestParam Long newSlotId,
            @RequestParam Integer newPax,
            Model model
    ) {
        Reservation reservation = reservationService.updateReservationSlot(
                reservationId,
                newSlotId,
                newPax
        );

        model.addAttribute("reservation", reservation);

        return "reservation-confirmation";
    }

    @GetMapping("/edit/options/contact")
    public String editContactForm(
            @RequestParam Long reservationId,
            Model model) {

        model.addAttribute("reservationId", reservationId);
        var reservationEditRequest = reservationService.getReservationEditRequest(reservationId);
        model.addAttribute("reservationEditRequest", reservationEditRequest);

        return "edit-contact";
    }

    @PostMapping("/edit/options/contact")
    public String updateContactForm(
            @RequestParam Long reservationId,
            @Valid @ModelAttribute ReservationEditRequest reservationEditRequest,
            BindingResult bindingResult,
            Model model
    ) {

        if (bindingResult.hasErrors()) {
            return "edit-contact";
        }

        var reservation = reservationService.updateReservationInformation(reservationId, reservationEditRequest);
        model.addAttribute("reservation", reservation);

        return "reservation-confirmation";
    }

    @GetMapping("/edit/options/cancel")
    public String cancelReservationCheck(
            @RequestParam Long reservationId,
            Model model
    ) {
        model.addAttribute("reservationId", reservationId);

        return "cancellation-request";
    }

    @PostMapping("/edit/options/cancel")
    public String cancelReservationConfirmation(
            @RequestParam Long reservationId,
            Model model
    ) {
        Reservation reservation = reservationService.cancelReservation(reservationId);
        model.addAttribute("reservation", reservation);

        return "reservation-cancelled";
    }

    @GetMapping("/pax/fragment")
    public String getPaxFragment(
            @RequestParam
            @DateTimeFormat(iso = DateTimeFormat.ISO.DATE)
            LocalDate date,
            @RequestParam Long boatId,
            @RequestParam(required = false) Long reservationId,
            Model model
    ) {
        Boat boat = boatService.findById(boatId);

        if (boat == null) {
            throw new IllegalArgumentException("Boat not found");
        }

        model.addAttribute("boat", boat);
        model.addAttribute("date", date);
        model.addAttribute("reservationId", reservationId);

        if (reservationId != null) {
            model.addAttribute(
                    "currentPax",
                    reservationService.getReservationPax(reservationId)
            );
        } else {
            model.addAttribute("currentPax", 1);
        }

        return "fragments/pax-fragment :: pax";
    }

}
