package com.lluisbauza.calypso.repository;

import com.lluisbauza.calypso.model.Client;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ClientRepository extends JpaRepository<Client, Long> {
}
