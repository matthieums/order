package com.switchfully.order.mapper;

import com.switchfully.order.domain.Item;
import com.switchfully.order.domain.OrderItem;
import com.switchfully.order.domain.Price;
import com.switchfully.order.dto.ItemGroupDto;
import com.switchfully.order.dto.ItemGroupReportDto;
import com.switchfully.order.exception.EntityNotFoundException;
import com.switchfully.order.service.ItemService;
import org.springframework.stereotype.Component;

import java.util.UUID;

import static com.switchfully.order.domain.OrderItem.OrderItemBuilder.orderItem;

@Component
public class OrderItemMapper {

    private ItemService itemService;

    public OrderItemMapper(ItemService itemService) {
        this.itemService = itemService;
    }

        public OrderItem toDomain(ItemGroupDto itemGroupDto) {
        return orderItem()
                .withItemId(UUID.fromString(itemGroupDto.getItemId()))
                .withOrderedAmount(itemGroupDto.getOrderedAmount())
                .withItemPrice(enrichWithItemPrice(itemGroupDto))
                .withShippingDateBasedOnAvailableItemStock(enrichWithItemAmountOfStock(itemGroupDto))
                .build();
    }

        public ItemGroupDto toDto(OrderItem orderItem) {
        return new ItemGroupDto()
                .withItemId(orderItem.getItemId().toString())
                .withOrderedAmount(orderItem.getOrderedAmount());
    }

    public ItemGroupReportDto toItemGroupReportDto(OrderItem orderItem) {
        return new ItemGroupReportDto()
                .withItemId(orderItem.getItemId().toString())
                .withOrderedAmount(orderItem.getOrderedAmount())
                .withName(enrichWithItemName(orderItem))
                .withTotalPrice(orderItem.getTotalPrice().getAmount().floatValue());
    }

    private Price enrichWithItemPrice(ItemGroupDto itemGroupDto) {
        return getItemForId(itemGroupDto.getItemId()).getPrice();
    }

    private int enrichWithItemAmountOfStock(ItemGroupDto itemGroupDto) {
        return getItemForId(itemGroupDto.getItemId()).getAmountOfStock();
    }

    private String enrichWithItemName(OrderItem orderItem) {
        return getItemForId(orderItem.getItemId().toString()).getName();
    }

    private Item getItemForId(String itemIdAsString) {
        Item item = itemService.getItem(UUID.fromString(itemIdAsString));
        if (item == null) {
            throw new EntityNotFoundException("mapping to an order of an item group (for creating a new order)",
                    Item.class, UUID.fromString(itemIdAsString));
        }
        return item;
    }

}
