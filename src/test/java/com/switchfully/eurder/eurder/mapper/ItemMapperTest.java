package com.switchfully.eurder.eurder.mapper;

import com.switchfully.eurder.eurder.api.dtos.CreateCustomerDTO;
import com.switchfully.eurder.eurder.api.dtos.CreateItemDTO;
import com.switchfully.eurder.eurder.api.dtos.ItemDTO;
import com.switchfully.eurder.eurder.domain.Customer;
import com.switchfully.eurder.eurder.domain.Item;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.*;

class ItemMapperTest {

    @Test
    void givenAnItemInstance_whenToDto_thenReturnCustomerDTO() {
        Item item = new Item(
                "Object",
                "An object",
                2.5d,
                10
        );

        ItemDTO expected = new ItemDTO(
                item.name(),
                item.description(),
                item.price(),
                item.amount()
        );

        assertThat(ItemMapper.toDTO(item))
                .usingRecursiveComparison()
                .isEqualTo(expected);
    }

    @Test
    void givenACreateItemDTO_whenToObject_thenReturnAnObjectWithAppropriateValues() {
        CreateItemDTO dto = new CreateItemDTO(
                "Object",
                "An object",
                2.5d,
                10
        );

        assertThat(ItemMapper.toObject(dto))
                .usingRecursiveComparison()
                .isEqualTo(
                        new Item(
                                dto.getName(),
                                dto.getDescription(),
                                dto.getPrice(),
                                dto.getAmount()
                        )
                );
    }
}

