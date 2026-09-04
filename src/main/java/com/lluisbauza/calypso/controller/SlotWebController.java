package com.lluisbauza.calypso.controller;

import com.lluisbauza.calypso.model.Slot;
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
    public SlotWebController(SlotService slotService) {
        this.slotService = slotService;
    }

    @GetMapping("/dates")
    public String getAvailableDates(Model model) {

        var dates = slotService.findAllAvailableDates();
        model.addAttribute("dates", dates);

        return "dates.html";

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

    @PostMapping("/slots")
    public String getDate(
            @RequestParam
            @DateTimeFormat(iso = DateTimeFormat.ISO.DATE)
            LocalDate date,
            Model model) {

        var slots =  slotService.findSlotsByDate(date);
        model.addAttribute("slots", slots);

        return "slots.html";

    }

}
