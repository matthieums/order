package com.switchfully.eurder.eurder.mapper;

import com.switchfully.eurder.eurder.api.dtos.CreateItemDTO;
import com.switchfully.eurder.eurder.api.dtos.CreateOrderDTO;
import com.switchfully.eurder.eurder.api.dtos.ItemDTO;
import com.switchfully.eurder.eurder.api.dtos.OrderDTO;
import com.switchfully.eurder.eurder.domain.Eurder;
import com.switchfully.eurder.eurder.domain.Item;

public class OrderMapper {

    public static OrderDTO toDTO(Eurder order) {
        if (order == null) return null;

        return new OrderDTO(
                order.getId(),
                order.getItemGroups(),
                order.getPlaced_by()
        );
    }

    public static Eurder toObject(CreateOrderDTO dto) {
        return new Eurder(
                dto.itemGroups(),
                dto.placedBy()
        );
    }


}
