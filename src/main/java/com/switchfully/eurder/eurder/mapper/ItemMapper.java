package com.switchfully.eurder.eurder.mapper;

import com.switchfully.eurder.eurder.api.dtos.CreateItemDTO;
import com.switchfully.eurder.eurder.api.dtos.ItemDTO;
import com.switchfully.eurder.eurder.domain.Item;
import org.springframework.stereotype.Component;

@Component
public class ItemMapper{

    public ItemDTO toDTO(Item item) {
        if (item == null) return null;

        return new ItemDTO(
                item.getId(),
                item.getName(),
                item.getDescription(),
                item.getPrice()
        );
    }

    public Item toObject(CreateItemDTO dto) {
        return new Item(
                dto.name(),
                dto.description(),
                dto.price(),
                dto.amount()
        );
    }
}
