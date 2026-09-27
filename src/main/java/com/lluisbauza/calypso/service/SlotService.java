package com.lluisbauza.calypso.service;

import com.lluisbauza.calypso.enums.SlotAvailability;
import com.lluisbauza.calypso.model.Reservation;
import com.lluisbauza.calypso.model.Slot;
import com.lluisbauza.calypso.repository.SlotRepository;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;

@Service
public class SlotService {

    private final SlotRepository slotRepository;

    public SlotService(SlotRepository slotRepository, TripService tripService) {
        this.slotRepository = slotRepository;
    }

    public Slot findById(Long id) {
        return slotRepository.findById(id).orElse(null);
    }
    public List<Slot> findAvailableSlotsByBoatIdAndDate(Long boatId, LocalDate date) {
        return slotRepository.findAvailableSlotsByBoatIdAndDate(boatId, date);
    }

    public List<LocalDate> findAllAvailableDates() {
        return slotRepository.findAllAvailableDates();
    }

    public Slot updateSlotBookedById(Long slotId) {

        var slot = slotRepository.findById(slotId).orElse(null);
        slot.setAvailability(SlotAvailability.BOOKED);
        return slotRepository.save(slot);

    }

    public void addReservationSlot(Reservation reservation) {
        reservation.getSlot();
    }

}
