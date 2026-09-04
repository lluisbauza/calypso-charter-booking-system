package com.lluisbauza.calypso.controller;

import com.lluisbauza.calypso.model.Slot;
import com.lluisbauza.calypso.service.SlotService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

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

    @GetMapping("/slots")
    public String getAvailableSlots(Model model) {

        var slots = slotService.findAvailableSlots();
        model.addAttribute("slots", slots);

        return "slots.html";

    }


}
