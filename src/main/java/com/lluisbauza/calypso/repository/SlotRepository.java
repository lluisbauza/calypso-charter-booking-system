package com.lluisbauza.calypso.repository;

import com.lluisbauza.calypso.model.Slot;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface SlotRepository extends JpaRepository<Slot, Long> {

    List<Slot> findByTripId(Long tripId);

}
