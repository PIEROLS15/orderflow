package com.nttdata.orderflow.auth;

import jakarta.validation.constraints.NotBlank;

public record TokenRequest(@NotBlank String clientId, @NotBlank String clientSecret) {
}
