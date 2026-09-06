package com.lluisbauza.calypso.repository;

import com.lluisbauza.calypso.model.Slot;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.time.LocalDate;
import java.util.List;

public interface SlotRepository extends JpaRepository<Slot, Long> {

    @Query("SELECT s FROM Slot s JOIN s.trip t JOIN t.boat b WHERE b.id = :boatId AND s.availability = AVAILABLE")
    List<Slot> findAvailableSlotsByBoatId(Long boatId);

    @Query("SELECT s FROM Slot s WHERE s.availability = AVAILABLE")
    List<Slot> findAvailableSlots();

    @Query("SELECT DISTINCT s.date FROM Slot s WHERE s.availability = AVAILABLE")
    List<LocalDate> findAllAvailableDates();

    List<Slot> findSlotsByDate(LocalDate date);

}
