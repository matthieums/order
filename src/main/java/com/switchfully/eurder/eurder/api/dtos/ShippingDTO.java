package com.switchfully.eurder.eurder.api.dtos;

import java.util.List;

public record ShippingDTO(List<ItemGroupDTO> itemGroups, String shippingAddress) {
}
