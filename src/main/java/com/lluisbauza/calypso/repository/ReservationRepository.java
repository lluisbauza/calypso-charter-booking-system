package com.lluisbauza.calypso.repository;

import com.lluisbauza.calypso.model.Reservation;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ReservationRepository extends JpaRepository<Reservation, Long> {
}
