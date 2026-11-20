package com.switchfully.order.mapper;

import com.switchfully.order.domain.Item;
import com.switchfully.order.dto.ItemOverviewDto;
import org.springframework.stereotype.Component;

@Component
public class ItemOverviewMapper {

        public ItemOverviewDto toDto(Item item) {
        return new ItemOverviewDto()
                .withId(item.getId())
                .withName(item.getName())
                .withAmountOfStock(item.getAmountOfStock())
                .withPrice(item.getPrice().getAmount().floatValue())
                .withStockUrgency(item.getStockUrgency().name());
    }

        public Item toDomain(ItemOverviewDto dtoObject) {
        throw new UnsupportedOperationException("Not yet implemented");
    }

}
