package com.lluisbauza.calypso.dto;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class ReservationSearchRequest {

    private String email;
    private String reservationCode;

}
