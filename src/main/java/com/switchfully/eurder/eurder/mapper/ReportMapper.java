package com.switchfully.eurder.eurder.mapper;

import com.switchfully.eurder.eurder.api.dtos.ItemGroupDTO;
import com.switchfully.eurder.eurder.api.dtos.OrderReportDTO;
import com.switchfully.eurder.eurder.api.dtos.ReportDTO;
import com.switchfully.eurder.eurder.domain.ItemGroup;
import com.switchfully.eurder.eurder.domain.user.Customer;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;
import java.util.List;

@Component
public class ReportMapper {
    private final ItemGroupMapper itemGroupMapper;

    public ReportMapper(ItemGroupMapper itemGroupMapper) {
        this.itemGroupMapper = itemGroupMapper;
    }

    public ReportDTO toDto(Customer customer, List<OrderReportDTO> DTOs) {
        return new ReportDTO(
            customer.getId(),
            DTOs
        );
    }

    public OrderReportDTO toOrderReportDTO(Long orderId, List<ItemGroup> itemGroups) {
        BigDecimal totalPrice = itemGroups.stream()
                .map(ItemGroup::calculatePrice)
                .reduce(BigDecimal.ZERO, BigDecimal::add);

        return new OrderReportDTO(
                orderId,
                itemGroupMapper.toDTO(itemGroups),
                totalPrice
        );
    }
}
