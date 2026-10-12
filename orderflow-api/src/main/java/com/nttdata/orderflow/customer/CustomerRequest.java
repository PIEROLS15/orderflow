package com.nttdata.orderflow.customer;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;

public record CustomerRequest(
    @NotBlank String firstName,
    @NotBlank String lastName,
    @Email @NotBlank String email,
    @NotBlank String documentNumber
) {
}
