package com.switchfully.eurder.eurder.mapper;

import com.switchfully.eurder.eurder.api.dtos.CreateItemGroupDTO;
import com.switchfully.eurder.eurder.api.dtos.ItemGroupDTO;
import com.switchfully.eurder.eurder.domain.Eurder;
import com.switchfully.eurder.eurder.domain.Item;
import com.switchfully.eurder.eurder.domain.ItemGroup;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class ItemGroupMapper {
    public ItemGroupDTO toDTO(ItemGroup itemGroup) {
        if (itemGroup == null) return null;

        return new ItemGroupDTO(
                itemGroup.getItem().getName(),
                itemGroup.getAmount(),
                itemGroup.getTotalPrice()
        );
    }

    public List<ItemGroupDTO> toDTO(List<ItemGroup> itemGroups) {
        if (itemGroups == null) return null;

        return itemGroups.stream()
                .map(this::toDTO)
                .toList();
    }
    public ItemGroup toDomain(Eurder order, CreateItemGroupDTO dto, Item item) {
        if (dto == null) return null;
        return new ItemGroup(
                order,
                item,
                dto.amount()
        );
    }

    public CreateItemGroupDTO toCreateItemGroupDTO(ItemGroup itemGroup) {
        return new CreateItemGroupDTO(
                itemGroup.getItem().getId(),
                itemGroup.getAmount()
        );
    }

    public List <CreateItemGroupDTO> toCreateItemGroupDTO(List<ItemGroup> itemGroups) {
        return itemGroups.stream()
                .map(this::toCreateItemGroupDTO)
                .toList();
    }
}
