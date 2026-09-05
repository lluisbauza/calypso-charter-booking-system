package com.lluisbauza.calypso.service;

import com.lluisbauza.calypso.model.Slot;
import com.lluisbauza.calypso.repository.SlotRepository;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;

@Service
public class SlotService {

    private final SlotRepository slotRepository;
    private final TripService tripService;

    public SlotService(SlotRepository slotRepository, TripService tripService) {
        this.slotRepository = slotRepository;
        this.tripService = tripService;
    }

    public List<Slot> findAvailableSlotsByBoatId(Long boatId) {
        return slotRepository.findAvailableSlotsByBoatId(boatId);
    }

    public List<Slot> findAvailableSlots() {
        return slotRepository.findAvailableSlots();
    }

    public List<LocalDate> findAllAvailableDates() {
        return slotRepository.findAllAvailableDates();
    }

    public List<Slot> findSlotsByDate(LocalDate date) {
        return slotRepository.findSlotsByDate(date);
    }

}
