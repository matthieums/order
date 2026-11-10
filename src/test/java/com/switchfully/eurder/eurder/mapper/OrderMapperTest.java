
package com.switchfully.eurder.eurder.mapper;

import com.switchfully.eurder.eurder.api.dtos.*;
import com.switchfully.eurder.eurder.domain.*;
import com.switchfully.eurder.eurder.domain.user.Customer;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class OrderMapperTest {

    private OrderMapper orderMapper;

    private Customer customer;
    private Eurder order;
    private Item item;
    private ItemGroup itemGroup;

    @BeforeEach
    void setUp() {
        ItemGroupMapper itemGroupMapper = new ItemGroupMapper();
        orderMapper = new OrderMapper(itemGroupMapper);

        customer = new Customer(
                "John", "Doe", "john@test.com",
                "password", "Street 1", "123456789"
        );

        order = new Eurder(customer);

        item = new Item(
                "Laptop", "Gaming laptop",
                new BigDecimal("100.00"), 10
        );

        itemGroup = new ItemGroup(order, item, 2);
    }

    @Test
    void toDetailsDTO_shouldReturnNull_whenOrderIsNull() {
        assertNull(orderMapper.toDetailsDTO(
                null, List.of(itemGroup), BigDecimal.ZERO
        ));
    }

    @Test
    void toDetailsDTO_shouldMapOrderCorrectly() {
        OrderDetailsDTO result = orderMapper.toDetailsDTO(
                order,
                List.of(itemGroup),
                new BigDecimal("200.00")
        );

        assertEquals(1, result.itemGroups().size());
        assertEquals("Laptop", result.itemGroups().get(0).itemName());
        assertEquals(new BigDecimal("200.00"), result.totalPrice());
    }

    @Test
    void toDetailsDTO_shouldMapOrderToOrderDTO() {
        OrderDTO result = orderMapper.toDetailsDTO(order);

        assertEquals(order.getId(), result.orderId());
        assertEquals(customer.getId(), result.customerId());
    }

    @Test
    void toCreateDTO_shouldMapItemGroupsCorrectly() {
        CreateOrderDTO result = orderMapper.toCreateDTO(
                List.of(itemGroup), customer.getId()
        );

        assertEquals(customer.getId(), result.customerId());
        assertEquals(1, result.itemGroups().size());
        assertEquals(2, result.itemGroups().get(0).amount());
    }

    @Test
    void toShippingDTO_shouldMapShippingInformationCorrectly() {
        ShippingDTO result = orderMapper.toShippingDTO(
                List.of(itemGroup)
        );

        assertEquals("Street 1", result.shippingAddress());
        assertEquals(1, result.itemGroups().size());
        assertEquals("Laptop", result.itemGroups().get(0).itemName());
    }
}
