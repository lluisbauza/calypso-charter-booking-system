package com.lluisbauza.calypso.repository;

import com.lluisbauza.calypso.model.Reservation;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.Optional;

public interface ReservationRepository extends JpaRepository<Reservation, Long> {

    @Query("SELECT r FROM Reservation r JOIN r.client c WHERE r.id = :reservationId AND r.status = CONFIRMED")
    Optional<Reservation> findByIdIfConfirmed(Long reservationId);

    @Query("SELECT r FROM Reservation r JOIN r.client c WHERE c.email = :email AND r.reservationCode = :reservationCode AND r.status = CONFIRMED")
    Reservation findByCodeAndEmailIfConfirmed(String reservationCode, String email);

}
