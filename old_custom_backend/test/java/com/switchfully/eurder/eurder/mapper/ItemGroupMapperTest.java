
package com.switchfully.eurder.eurder.mapper;

import com.switchfully.eurder.eurder.api.dtos.CreateItemGroupDTO;
import com.switchfully.eurder.eurder.api.dtos.ItemGroupDTO;
import com.switchfully.eurder.eurder.domain.Eurder;
import com.switchfully.eurder.eurder.domain.Item;
import com.switchfully.eurder.eurder.domain.ItemGroup;
import com.switchfully.eurder.eurder.domain.user.Customer;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class ItemGroupMapperTest {

    private ItemGroupMapper itemGroupMapper;

    private Customer customer;
    private Eurder order;
    private Item item;

    @BeforeEach
    void setUp() {
        itemGroupMapper = new ItemGroupMapper();

        customer = new Customer(
                "John", "Doe", "john@test.com",
                "password", "Street 1", "123456789"
        );

        order = new Eurder(customer);

        item = new Item(
                "Laptop", "Gaming laptop",
                new BigDecimal("100.00"), 10
        );
    }

    @Test
    void toDTO_shouldMapItemGroupCorrectly() {
        ItemGroup itemGroup = new ItemGroup(order, item, 2);

        ItemGroupDTO result = itemGroupMapper.toDTO(itemGroup);

        assertEquals("Laptop", result.itemName());
        assertEquals(2, result.orderedAmount());
        assertEquals(new BigDecimal("200.00"), result.totalPrice());
    }

    @Test
    void toDTO_shouldReturnNull_whenItemGroupIsNull() {
        assertNull(itemGroupMapper.toDTO((ItemGroup) null));
    }

    @Test
    void toDTO_shouldMapListOfItemGroups() {
        ItemGroup group1 = new ItemGroup(order, item, 2);
        ItemGroup group2 = new ItemGroup(order, item, 3);

        List<ItemGroupDTO> result =
                itemGroupMapper.toDTO(List.of(group1, group2));

        assertEquals(2, result.size());
        assertEquals(new BigDecimal("200.00"), result.get(0).totalPrice());
        assertEquals(new BigDecimal("300.00"), result.get(1).totalPrice());
    }

    @Test
    void toDTO_shouldReturnNull_whenListIsNull() {
        assertNull(itemGroupMapper.toDTO((List<ItemGroup>) null));
    }

    @Test
    void toDomain_shouldCreateItemGroupCorrectly() {
        CreateItemGroupDTO dto = new CreateItemGroupDTO(2L, 3);

        ItemGroup result = itemGroupMapper.toDomain(order, dto, item);

        assertEquals(order, result.getEurder());
        assertEquals(item, result.getItem());
        assertEquals(3, result.getAmount());
        assertEquals(item.getPrice(), result.calculatePrice()
                .divide(BigDecimal.valueOf(3)));
    }

    @Test
    void toCreateItemGroupDTO_shouldMapItemGroupCorrectly() {
        ItemGroup itemGroup = new ItemGroup(order, item, 2);

        CreateItemGroupDTO result =
                itemGroupMapper.toCreateItemGroupDTO(itemGroup);

        assertEquals(2, result.amount());
        assertEquals(item.getId(), result.itemId());
    }

    @Test
    void toCreateItemGroupDTO_shouldMapListCorrectly() {
        ItemGroup group1 = new ItemGroup(order, item, 2);
        ItemGroup group2 = new ItemGroup(order, item, 3);

        List<CreateItemGroupDTO> result =
                itemGroupMapper.toCreateItemGroupDTO(
                        List.of(group1, group2)
                );

        assertEquals(2, result.size());
        assertEquals(2, result.get(0).amount());
        assertEquals(3, result.get(1).amount());
    }
}
