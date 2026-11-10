package com.switchfully.eurder.eurder.api.dtos;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import jakarta.validation.constraints.PositiveOrZero;

import java.math.BigDecimal;

public record CreateItemDTO(@NotBlank String name, @NotBlank String description,
                            @NotNull BigDecimal price, @PositiveOrZero int amount) {
}
