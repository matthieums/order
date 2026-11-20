package com.switchfully.eurder.eurder.api.dtos;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;

public record CreateItemGroupDTO(@NotNull Long itemId,
                                 @Positive int amount) {}
