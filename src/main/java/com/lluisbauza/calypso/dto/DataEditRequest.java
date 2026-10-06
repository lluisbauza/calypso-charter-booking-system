package com.lluisbauza.calypso.dto;

import com.lluisbauza.calypso.model.Slot;

public record DataEditRequest(
        String reservationCode,
        Slot slot
) {
}
