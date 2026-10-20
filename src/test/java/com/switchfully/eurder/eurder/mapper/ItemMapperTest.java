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
                item.getName(),
                item.getDescription(),
                item.getPrice(),
                item.getAmount()
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

        Item item = ItemMapper.toObject(dto);

        assertThat(item.getName()).isEqualTo(dto.getName());
        assertThat(item.getDescription()).isEqualTo(dto.getDescription());
        assertThat(item.getPrice()).isEqualTo(dto.getPrice());
        assertThat(item.getAmount()).isEqualTo(dto.getAmount());
        assertThat(item.getId()).isNotNull();
    }
}

