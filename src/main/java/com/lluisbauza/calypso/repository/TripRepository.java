package com.lluisbauza.calypso.repository;

import com.lluisbauza.calypso.model.Trip;
import org.springframework.data.jpa.repository.JpaRepository;

public interface TripRepository extends JpaRepository<Trip, Long> {
}
