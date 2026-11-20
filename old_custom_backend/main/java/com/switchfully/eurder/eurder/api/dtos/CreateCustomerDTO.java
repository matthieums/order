package com.switchfully.eurder.eurder.api.dtos;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;

public record CreateCustomerDTO(@NotBlank String firstname, @NotBlank String lastname, @NotBlank @Email String email,
                                @NotBlank String address, @NotBlank String phoneNumber, @NotBlank String password) {
}
