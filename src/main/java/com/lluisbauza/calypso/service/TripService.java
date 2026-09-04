package com.lluisbauza.calypso.service;

import com.lluisbauza.calypso.model.Trip;
import com.lluisbauza.calypso.repository.TripRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class TripService {

    private final TripRepository tripRepository;
    public TripService(TripRepository tripRepository) {
        this.tripRepository = tripRepository;
    }

    public List<Trip> getTripsByBoatId(Long boatId) {
        return tripRepository.findByBoatId(boatId);
    }


}
