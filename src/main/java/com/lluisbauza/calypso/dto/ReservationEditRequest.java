package com.lluisbauza.calypso.dto;

public record ReservationEditRequest(
        Long clientId,
        String firstName,
        String lastName,
        String email,
        String phoneNumber,
        Integer pax
) {
}
