package com.lluisbauza.calypso.repository;

import com.lluisbauza.calypso.model.Reservation;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

public interface ReservationRepository extends JpaRepository<Reservation, Long> {

    @Query("SELECT r FROM Reservation r JOIN r.client c WHERE c.email = :email AND r.reservationCode = :reservationCode")
    Reservation findByCodeAndEmail(String reservationCode, String email);
}
