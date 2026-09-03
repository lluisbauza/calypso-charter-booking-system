package com.lluisbauza.calypso.service;

import com.lluisbauza.calypso.model.Slot;
import com.lluisbauza.calypso.repository.SlotRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class SlotService {

    private final SlotRepository slotRepository;

    public SlotService(SlotRepository slotRepository) {
        this.slotRepository = slotRepository;
    }


}
