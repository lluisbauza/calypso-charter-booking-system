package com.lluisbauza.calypso.controller;

import com.lluisbauza.calypso.repository.SlotRepository;
import com.lluisbauza.calypso.service.SlotService;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/slots")
public class SlotRestController {

    private final SlotService slotService;
    public SlotRestController(SlotService slotService) {
        this.slotService = slotService;
    }


}
