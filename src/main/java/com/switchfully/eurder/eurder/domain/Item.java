package com.switchfully.eurder.eurder.domain;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Positive;
import jakarta.validation.constraints.PositiveOrZero;

public record Item(
        @NotBlank String name,
        @NotBlank String description,
        @Positive double price,
        @PositiveOrZero int amount
) {
}
