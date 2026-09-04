package com.lluisbauza.calypso.repository;

import com.lluisbauza.calypso.enums.SlotAvailability;
import com.lluisbauza.calypso.model.Boat;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.time.LocalDate;
import java.util.List;

public interface BoatRepository extends JpaRepository<Boat, Long> {

    @Query("SELECT DISTINCT s.trip.boat\n" +
            "FROM Slot s\n" +
            "WHERE s.date = :date\n" +
            "AND s.availability = :availability")
    List<Boat> findBoatAvailableByDate(LocalDate date, SlotAvailability availability);

}
