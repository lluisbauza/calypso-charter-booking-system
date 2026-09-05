package com.lluisbauza.calypso.controller;

import com.lluisbauza.calypso.enums.SlotAvailability;
import com.lluisbauza.calypso.model.Slot;
import com.lluisbauza.calypso.service.BoatService;
import com.lluisbauza.calypso.service.SlotService;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.time.LocalDate;
import java.util.List;

@Controller
public class SlotWebController {

    private final SlotService slotService;
    private final BoatService boatService;
    public SlotWebController(SlotService slotService,  BoatService boatService) {
        this.slotService = slotService;
        this.boatService = boatService;
    }

    @GetMapping("/dates")
    public String getAvailableDates(Model model) {

        var dates = slotService.findAllAvailableDates();
        model.addAttribute("dates", dates);

        return "dates.html";

    }

    @PostMapping("/boats")
    public String getAvailableBoats(
            @RequestParam
            @DateTimeFormat(iso = DateTimeFormat.ISO.DATE)
            LocalDate date,
            Model model) {

        var boats =  boatService.findBoatAvailableByDate(date, SlotAvailability.AVAILABLE);
        model.addAttribute("boats", boats);

        return "boats.html";

    }

    @PostMapping("/slots")
    public String getAvailableSlotsByBoatId(
            @RequestParam Long boatId,
            Model model
    ) {

        var slots = slotService.findAvailableSlotsByBoatId(boatId);
        model.addAttribute("slots", slots);

        return "slots.html";
    }

//    @GetMapping("/slots")
//    public String getAvailableSlots(Model model) {
//
//        var slots = slotService.findAvailableSlots();
//        model.addAttribute("slots", slots);
//
//        return "slots.html";
//
//    }

//    @PostMapping("/slots")
//    public String getDate(
//            @RequestParam
//            @DateTimeFormat(iso = DateTimeFormat.ISO.DATE)
//            LocalDate date,
//            Model model) {
//
//        var slots =  slotService.findSlotsByDate(date);
//        model.addAttribute("slots", slots);
//
//        return "slots.html";
//
//    }

}
