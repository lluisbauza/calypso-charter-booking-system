package com.lluisbauza.calypso.service;

import com.lluisbauza.calypso.repository.TripRepository;
import org.springframework.stereotype.Service;

@Service
public class TripService {

    private final TripRepository tripRepository;
    public TripService(TripRepository tripRepository) {
        this.tripRepository = tripRepository;
    }

}
