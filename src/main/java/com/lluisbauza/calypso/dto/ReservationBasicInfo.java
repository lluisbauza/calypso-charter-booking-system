package com.lluisbauza.calypso.dto;

import java.time.LocalDate;
import java.time.LocalTime;

public record ReservationBasicInfo(
        String reservationCode,
        LocalDate date,
        LocalTime departureTime,
        String boatName
) {
}
