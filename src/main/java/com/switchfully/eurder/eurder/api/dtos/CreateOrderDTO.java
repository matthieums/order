package com.switchfully.eurder.eurder.api.dtos;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;

import java.util.List;

public record CreateOrderDTO(@NotEmpty List<CreateItemGroupDTO> itemGroups,  @NotNull Long customerId) {}
