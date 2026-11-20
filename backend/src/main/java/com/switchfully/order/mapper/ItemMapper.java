package com.switchfully.order.mapper;

import com.switchfully.order.domain.Item;
import com.switchfully.order.domain.Price;
import com.switchfully.order.dto.ItemDto;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;
import java.util.UUID;

import static com.switchfully.order.domain.Item.ItemBuilder.item;

@Component
public class ItemMapper {

    public Item toDomain(UUID itemId, ItemDto itemDto) {
        if(itemDto.getId() == null) {
            return toDomain(itemDto.withId(itemId));
        }
        if(!itemId.toString().equals(itemDto.getId())) {
            throw new IllegalArgumentException("When updating an item, the provided ID in the path should match the ID in the body: " +
                    "ID in path = " + itemId.toString() + ", ID in body = " + itemDto.getId());
        }
        return toDomain(itemDto);
    }

        public Item toDomain(ItemDto itemDto) {
        return item()
                .withId(itemDto.getId() == null ? null : UUID.fromString(itemDto.getId()))
                .withName(itemDto.getName())
                .withDescription(itemDto.getDescription())
                .withAmountOfStock(itemDto.getAmountOfStock())
                .withPrice(Price.create(BigDecimal.valueOf(itemDto.getPrice())))
                .build();
    }

        public ItemDto toDto(Item item) {
        return new ItemDto()
                .withId(item.getId())
                .withName(item.getName())
                .withDescription(item.getDescription())
                .withAmountOfStock(item.getAmountOfStock())
                .withPrice(item.getPrice().getAmount().floatValue())
                .withStockUrgency(item.getStockUrgency().name());
    }

}
