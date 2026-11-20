package com.switchfully.eurder.eurder.api.dtos;

import java.math.BigDecimal;
import java.util.List;

public record OrderDetailsDTO(Long id, List<ItemGroupDTO> itemGroups, BigDecimal totalPrice) {
}