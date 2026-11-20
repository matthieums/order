package com.switchfully.order.mapper;

import com.switchfully.order.domain.Address;
import com.switchfully.order.domain.Order;
import com.switchfully.order.dto.ItemGroupDto;
import com.switchfully.order.dto.OrderAfterCreationDto;
import com.switchfully.order.dto.OrderCreationDto;
import com.switchfully.order.dto.OrderDto;
import com.switchfully.order.dto.OrdersReportDto;
import com.switchfully.order.dto.SingleOrderReportDto;
import com.switchfully.order.service.CustomerService;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

import static com.switchfully.order.domain.Order.OrderBuilder.order;

@Component
public class OrderMapper {

    private final OrderItemMapper orderItemMapper;
    private final AddressMapper addressMapper;
    private final CustomerService customerService;

    public OrderMapper(OrderItemMapper orderItemMapper, AddressMapper addressMapper, CustomerService customerService) {
        this.orderItemMapper = orderItemMapper;
        this.addressMapper = addressMapper;
        this.customerService = customerService;
    }

    public OrderDto toDto(Order order) {
        return new OrderDto()
                .withOrderId(order.getId().toString())
                .withItemGroups(order.getOrderItems().stream()
                        .map(orderItem -> orderItemMapper.toDto(orderItem))
                        .toArray(ItemGroupDto[]::new))
                .withAddress(addressMapper.toDto(getAddressForCustomer(order.getCustomerId())));
    }

    private Address getAddressForCustomer(UUID customerId) {
        return customerService.getCustomer(customerId).getAddress();
    }

    public Order toDomain(OrderCreationDto orderCreationDto) {
        return order()
                .withCustomerId(UUID.fromString(orderCreationDto.getCustomerId()))
                .withOrderItems(orderCreationDto.getItemGroups().stream()
                        .map(orderItemMapper::toDomain)
                        .collect(Collectors.toList()))
                .build();
    }

    public OrderAfterCreationDto toOrderAfterCreationDto(Order order) {
        return new OrderAfterCreationDto()
                .withOrderId(order.getId().toString())
                .withTotalPrice(order.getTotalPrice().getAmount().floatValue());
    }

    public OrdersReportDto toOrdersReportDto(List<Order> orders) {
        return new OrdersReportDto()
                .withOrders(orders.stream()
                        .map(this::toSingleOrderReportDto)
                        .collect(Collectors.toList()))
                .withTotalPriceOfAllOrders(orders.stream()
                        .map(order -> order.getTotalPrice().getAmount().floatValue())
                        .reduce(0f, Float::sum));
    }

    private SingleOrderReportDto toSingleOrderReportDto(Order order) {
        return new SingleOrderReportDto()
                .withOrderId(order.getId().toString())
                .withItemGroups(order.getOrderItems().stream()
                        .map(orderItemMapper::toItemGroupReportDto)
                        .collect(Collectors.toList()));
    }
}
