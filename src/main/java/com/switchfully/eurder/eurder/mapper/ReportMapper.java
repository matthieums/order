package com.switchfully.eurder.eurder.mapper;

import com.switchfully.eurder.eurder.api.dtos.OrderReportDTO;
import com.switchfully.eurder.eurder.api.dtos.ReportDTO;
import com.switchfully.eurder.eurder.domain.Eurder;
import com.switchfully.eurder.eurder.domain.ItemGroup;
import com.switchfully.eurder.eurder.domain.user.Customer;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Map;

@Component
public class ReportMapper {
    private OrderMapper orderMapper;

    public ReportDTO toDto(Customer customer, List<OrderReportDTO> DTOs) {
        return new ReportDTO(
            customer.getId(),
            DTOs
        );
    }

    public OrderReportDTO toOrderReportDTO(Customer customer, Eurder order, List<ItemGroup> itemGroups) {
        return new OrderReportDTO(customer, order, itemGroups);
    }
}
