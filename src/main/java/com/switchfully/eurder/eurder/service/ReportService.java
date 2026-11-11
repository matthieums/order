package com.switchfully.eurder.eurder.service;

import com.switchfully.eurder.eurder.api.dtos.OrderReportDTO;
import com.switchfully.eurder.eurder.api.dtos.ReportDTO;
import com.switchfully.eurder.eurder.api.dtos.ShippingDTO;
import com.switchfully.eurder.eurder.domain.Eurder;
import com.switchfully.eurder.eurder.domain.ItemGroup;
import com.switchfully.eurder.eurder.domain.user.Customer;
import com.switchfully.eurder.eurder.mapper.OrderMapper;
import com.switchfully.eurder.eurder.mapper.ReportMapper;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
public class ReportService {
    private final CustomerService customerService;
    private final OrderService orderService;
    private final ItemGroupService itemGroupService;

    private final ReportMapper reportMapper;
    private final OrderMapper orderMapper;

    public ReportService(
            CustomerService customerService,
            OrderService orderService,
            ItemGroupService itemGroupService,
            ReportMapper reportMapper,
            OrderMapper orderMapper
    ) {
        this.customerService = customerService;
        this.orderService = orderService;
        this.itemGroupService = itemGroupService;
        this.reportMapper = reportMapper;
        this.orderMapper = orderMapper;
    }

    public ReportDTO getReportForCustomerWithId(Long id) {
        Customer customer = customerService.findById(id);
        List<Eurder> orders = orderService.findAllByCustomerId(id);

        List<OrderReportDTO> orderReportsDTOs = orders.stream()
                .map(order -> {
                    List<ItemGroup> itemGroups =
                            itemGroupService.findAllByEurder_Id(order.getId());
                    return reportMapper.toOrderReportDTO(order.getId(), itemGroups);
                })
                .toList();

        return reportMapper.toDto(customer, orderReportsDTOs);
    }

    public List<ShippingDTO> getOrdersWithItemsShippingToday() {
        List<ItemGroup> itemGroupsShippingToday =
                itemGroupService.findAllByShippingDate(LocalDate.now());

        Map<Long, List<ItemGroup>> groupsByOrderId = itemGroupsShippingToday.stream()
                .collect(Collectors.groupingBy(
                        group -> group.getEurder().getId()
                ));

        return groupsByOrderId.values().stream()
                .map(orderMapper::toShippingDTO)
                .toList();
    }
}
