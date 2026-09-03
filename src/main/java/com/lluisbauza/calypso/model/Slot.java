package com.lluisbauza.calypso.model;

import com.lluisbauza.calypso.enums.SlotAvailability;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDate;
import java.time.LocalTime;

@Entity
@Getter @Setter
@AllArgsConstructor
@NoArgsConstructor
public class Slot {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    private Trip trip;

    @Column(nullable = false)
    private LocalDate date;

    @Column(nullable = false)
    private LocalTime departureTime;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private SlotAvailability availability;

}
