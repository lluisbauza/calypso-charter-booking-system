package com.lluisbauza.calypso.dto;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter @Setter
@NoArgsConstructor
public class ReservationRequest {

    private Long clientId;
    private Long slotId;
    private String firstName;
    private String lastName;
    private String email;
    private String phoneNumber;

}
