package com.lluisbauza.calypso.dto;

import java.time.LocalDate;
import java.time.LocalTime;

public record ReservationEmailData(
        String clientName,
        String reservationCode,
        LocalDate date,
        LocalTime departureTime,
        Integer pax
) {
}
