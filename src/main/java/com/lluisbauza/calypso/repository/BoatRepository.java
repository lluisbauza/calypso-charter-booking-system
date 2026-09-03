package com.lluisbauza.calypso.repository;

import com.lluisbauza.calypso.model.Boat;
import org.springframework.data.jpa.repository.JpaRepository;

public interface BoatRepository extends JpaRepository<Boat, Long> {
}
