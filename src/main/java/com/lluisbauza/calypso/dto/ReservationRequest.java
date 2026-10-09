package com.lluisbauza.calypso.dto;

import jakarta.validation.constraints.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter @Setter
@NoArgsConstructor
public class ReservationRequest {

    @NotNull
    private Long slotId;

    @NotBlank
    @Size(max=50)
    private String firstName;

    @NotBlank
    @Size(max=50)
    private String lastName;

    @NotBlank @Email(regexp = "^[A-Za-z0-9._%+-]+@[A-Za-z0-9.-]+\\.[A-Za-z]{2,}$")
    @Size(max=254)
    private String email;

    @NotBlank
    @Pattern(regexp = "^\\+?[0-9][0-9 ]{8,18}$")
    private String phoneNumber;

    @NotNull
    @Positive
    private Integer pax;

}
