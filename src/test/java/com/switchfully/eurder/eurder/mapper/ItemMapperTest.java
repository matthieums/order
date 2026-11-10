package com.switchfully.eurder.eurder.mapper;

import com.switchfully.eurder.eurder.api.dtos.CreateItemDTO;
import com.switchfully.eurder.eurder.api.dtos.ItemDTO;
import com.switchfully.eurder.eurder.domain.Item;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;

import static org.junit.jupiter.api.Assertions.*;

class ItemMapperTest {

    private ItemMapper itemMapper;

    @BeforeEach
    void setUp() {
        itemMapper = new ItemMapper();
    }

    @Test
    void toDTO_shouldMapItemCorrectly() {
        Item item = new Item(
                "Laptop", "Gaming laptop",
                new BigDecimal("1200.00"), 5
        );

        ItemDTO result = itemMapper.toDTO(item);

        assertEquals(item.getName(), result.name());
        assertEquals(item.getDescription(), result.description());
        assertEquals(item.getPrice(), result.price());
    }

    @Test
    void toDTO_shouldReturnNull_whenItemIsNull() {
        assertNull(itemMapper.toDTO(null));
    }

    @Test
    void toObject_shouldConvertCreateItemDTOToItem() {
        CreateItemDTO dto = new CreateItemDTO(
                "Laptop", "Gaming laptop",
                new BigDecimal("1200.00"), 5
        );

        Item result = itemMapper.toObject(dto);

        assertEquals(dto.name(), result.getName());
        assertEquals(dto.description(), result.getDescription());
        assertEquals(dto.price(), result.getPrice());
        assertEquals(dto.amount(), result.getAmount());
    }
}