package com.lluisbauza.calypso.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;

public record ReservationEditRequest(

        @NotBlank
        @Size(max=50)
        String firstName,

        @NotBlank
        @Size(max=50)
        String lastName,

        @NotBlank @Email(regexp = "^[A-Za-z0-9._%+-]+@[A-Za-z0-9.-]+\\.[A-Za-z]{2,}$")
        @Size(max=254)
        String email,

        @NotBlank
        @Pattern(regexp = "^\\+?[0-9][0-9 ]{8,18}$")
        String phoneNumber
) {
}
