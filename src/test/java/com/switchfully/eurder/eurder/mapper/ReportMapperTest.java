
package com.switchfully.eurder.eurder.mapper;

import com.switchfully.eurder.eurder.api.dtos.ItemGroupDTO;
import com.switchfully.eurder.eurder.api.dtos.OrderReportDTO;
import com.switchfully.eurder.eurder.api.dtos.ReportDTO;
import com.switchfully.eurder.eurder.domain.Eurder;
import com.switchfully.eurder.eurder.domain.Item;
import com.switchfully.eurder.eurder.domain.ItemGroup;
import com.switchfully.eurder.eurder.domain.user.Customer;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class ReportMapperTest {

    private ReportMapper reportMapper;

    private Customer customer;
    private Eurder order;
    private Item item1;
    private Item item2;

    @BeforeEach
    void setUp() {
        reportMapper = new ReportMapper(new ItemGroupMapper());

        customer = new Customer(
                "John", "Doe", "john@test.com",
                "password", "Street 1", "123456789"
        );

        order = new Eurder(customer);

        item1 = new Item(
                "Laptop", "Gaming laptop",
                new BigDecimal("100.00"), 10
        );

        item2 = new Item(
                "Mouse", "Gaming mouse",
                new BigDecimal("25.00"), 20
        );
    }

    @Test
    void toDto_shouldMapCustomerAndOrdersCorrectly() {
        OrderReportDTO orderReport = new OrderReportDTO(
                1L, List.of(), BigDecimal.ZERO
        );

        ReportDTO result = reportMapper.toDto(
                customer, List.of(orderReport)
        );

        assertEquals(customer.getId(), result.customerId());
        assertEquals(1, result.orders().size());
        assertEquals(orderReport, result.orders().get(0));
    }

    @Test
    void toOrderReportDTO_shouldCalculateTotalPrice() {
        ItemGroup group1 = new ItemGroup(order, item1, 2);
        ItemGroup group2 = new ItemGroup(order, item2, 3);

        OrderReportDTO result = reportMapper.toOrderReportDTO(
                1L, List.of(group1, group2)
        );

        assertEquals(1L, result.orderId());
        assertEquals(new BigDecimal("275.00"), result.totalPrice());
    }

    @Test
    void toOrderReportDTO_shouldReturnZero_whenNoItemGroups() {
        OrderReportDTO result = reportMapper.toOrderReportDTO(
                1L, List.of()
        );

        assertEquals(BigDecimal.ZERO, result.totalPrice());
        assertTrue(result.itemGroups().isEmpty());
    }

    @Test
    void toOrderReportDTO_shouldMapIndividualItemGroups() {
        ItemGroup group = new ItemGroup(order, item1, 2);

        OrderReportDTO result = reportMapper.toOrderReportDTO(
                1L, List.of(group)
        );

        ItemGroupDTO mappedGroup = result.itemGroups().get(0);

        assertEquals("Laptop", mappedGroup.itemName());
        assertEquals(2, mappedGroup.orderedAmount());
        assertEquals(new BigDecimal("200.00"), mappedGroup.totalPrice());
    }
}
