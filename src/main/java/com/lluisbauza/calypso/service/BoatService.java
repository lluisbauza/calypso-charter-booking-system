package com.lluisbauza.calypso.service;

import com.lluisbauza.calypso.enums.SlotAvailability;
import com.lluisbauza.calypso.model.Boat;
import com.lluisbauza.calypso.model.Reservation;
import com.lluisbauza.calypso.repository.BoatRepository;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;

@Service
public class BoatService {

    private final BoatRepository boatRepository;

    public BoatService(BoatRepository boatRepository) {
        this.boatRepository = boatRepository;
    }

    public List<Boat> findBoatAvailableByDate(LocalDate date, SlotAvailability availability) {
        return boatRepository.findBoatAvailableByDate(date, availability);
    }

}
