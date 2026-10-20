package com.switchfully.eurder.eurder.mapper;

import com.switchfully.eurder.eurder.api.dtos.CreateItemDTO;
import com.switchfully.eurder.eurder.api.dtos.ItemDTO;
import com.switchfully.eurder.eurder.domain.Item;

public class ItemMapper{

    public static ItemDTO toDTO(Item item) {
        if (item == null) return null;

        return new ItemDTO(
                item.getId(),
                item.getName(),
                item.getDescription(),
                item.getPrice(),
                item.getAmount()
        );
    }

    public static Item toObject(CreateItemDTO dto) {
        return new Item(
                dto.getName(),
                dto.getDescription(),
                dto.getPrice(),
                dto.getAmount()
        );
    }
}
