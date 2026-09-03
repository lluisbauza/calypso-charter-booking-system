package com.lluisbauza.calypso.repository;

import com.lluisbauza.calypso.model.Trip;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface TripRepository extends JpaRepository<Trip, Long> {

    List<Trip> findByBoatId(Long boatId);


}
