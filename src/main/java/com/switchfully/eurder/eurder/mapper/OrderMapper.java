package com.switchfully.eurder.eurder.mapper;

import com.switchfully.eurder.eurder.api.dtos.CreateOrderDTO;
import com.switchfully.eurder.eurder.api.dtos.OrderDTO;
import com.switchfully.eurder.eurder.api.dtos.OrderDetailsDTO;
import com.switchfully.eurder.eurder.api.dtos.ShippingDTO;
import com.switchfully.eurder.eurder.domain.Eurder;
import com.switchfully.eurder.eurder.domain.ItemGroup;

import java.util.List;

public class OrderMapper {
    private ItemGroupMapper itemGroupMapper;

    public OrderDetailsDTO toDetailsDTO(Eurder order, List<ItemGroup> itemGroups, double totalPrice) {
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
        return new ShippingDTO(itemGroupMapper.toDTO(itemGroups), itemGroups.getFirst().getEurder().getCustomer().getAddress());
    }

}
