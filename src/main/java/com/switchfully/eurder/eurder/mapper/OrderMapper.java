package com.switchfully.eurder.eurder.mapper;

import com.switchfully.eurder.eurder.api.dtos.CreateOrderDTO;
import com.switchfully.eurder.eurder.api.dtos.OrderDTO;
import com.switchfully.eurder.eurder.api.dtos.OrderDetailsDTO;
import com.switchfully.eurder.eurder.api.dtos.ShippingDTO;
import com.switchfully.eurder.eurder.domain.Eurder;
import com.switchfully.eurder.eurder.domain.ItemGroup;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;
import java.util.List;

@Component
public class OrderMapper {
    private final ItemGroupMapper itemGroupMapper;

    public OrderMapper(ItemGroupMapper itemGroupMapper) {
        this.itemGroupMapper = itemGroupMapper;
    }

    public OrderDetailsDTO toDetailsDTO(Eurder order, List<ItemGroup> itemGroups, BigDecimal totalPrice) {
        if (order == null) return null;

        return new OrderDetailsDTO(
                order.getId(),
                itemGroupMapper.toDTO(itemGroups),
                totalPrice
        );
    }

    public OrderDTO toDetailsDTO(Eurder order) {
        return new OrderDTO(order.getId(), order.getCustomer().getId());
    }

    public CreateOrderDTO toCreateDTO(List<ItemGroup> itemGroups, Long customerId) {
        return new CreateOrderDTO(
                itemGroupMapper.toCreateItemGroupDTO(itemGroups),
                customerId
        );
    }

    public ShippingDTO toShippingDTO(List<ItemGroup> itemGroups) {
        if (itemGroups == null || itemGroups.isEmpty()) {
            throw new IllegalArgumentException(
                    "Cannot create shipping DTO without item groups"
            );
        }
        return new ShippingDTO(
                itemGroupMapper.toDTO(itemGroups),
                itemGroups.getFirst()
                        .getEurder()
                        .getCustomer()
                        .getAddress()
        );
    }

}
