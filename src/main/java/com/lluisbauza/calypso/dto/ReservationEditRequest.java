package com.lluisbauza.calypso.dto;

public record ReservationEditRequest(

        String firstName,
        String lastName,
        String email,
        String phoneNumber,
        Integer pax
) {
}
