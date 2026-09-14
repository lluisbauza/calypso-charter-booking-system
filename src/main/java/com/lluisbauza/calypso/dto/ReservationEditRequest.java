package com.lluisbauza.calypso.dto;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class ReservationEditRequest {

    private String email;
    private String reservationCode;

}
