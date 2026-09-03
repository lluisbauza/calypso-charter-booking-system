package com.lluisbauza.calypso.model;

import com.lluisbauza.calypso.enums.ReservationStatus;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Getter @Setter
@AllArgsConstructor
@NoArgsConstructor
public class Reservation {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(unique = true, nullable = false)
    private String reservationCode;

    @ManyToOne
    private Client client;

    @ManyToOne
    private Slot slot;

    @Enumerated(EnumType.STRING)
    private ReservationStatus status;

}
