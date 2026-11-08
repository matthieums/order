package com.switchfully.eurder.eurder.api.dtos;

import java.util.List;

public record OrderDetailsDTO(Long id, List<ItemGroupDTO> itemGroups, double totalPrice) {
}