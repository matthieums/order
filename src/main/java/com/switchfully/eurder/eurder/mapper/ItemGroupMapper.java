package com.switchfully.eurder.eurder.mapper;

import com.switchfully.eurder.eurder.api.dtos.ItemGroupDTO;
import com.switchfully.eurder.eurder.domain.ItemGroup;

import java.util.List;

public class ItemGroupMapper {
    public static ItemGroupDTO toDTO(ItemGroup itemGroup) {
        if (itemGroup == null) return null;

        return new ItemGroupDTO(
                itemGroup.getItemName(),
                itemGroup.getAmount(),
                itemGroup.getTotalPrice()
        );
    }

    public static List<ItemGroupDTO> toDTO(List<ItemGroup> itemGroups) {
        if (itemGroups == null) return null;

        return itemGroups.stream()
                .map(ItemGroupMapper::toDTO)
                .toList();
    }
}
