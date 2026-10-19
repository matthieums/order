package com.switchfully.eurder.eurder.domain;

import jakarta.validation.constraints.NotBlank;

public record Customer(
        @NotBlank String firstname,
        @NotBlank String lastname,
        @NotBlank String email,
        @NotBlank String address,
        @NotBlank String phoneNumber
) {
}