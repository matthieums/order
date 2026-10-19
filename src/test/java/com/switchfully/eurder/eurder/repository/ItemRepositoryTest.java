package com.switchfully.eurder.eurder.repository;

import com.switchfully.eurder.eurder.domain.Item;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class ItemRepositoryTest {

    @Test
    void givenAnItem_whenAdd_thenItemExistsInRepository(){
        ItemRepository itemRepository = new ItemRepository();
        Item newItem = new Item(
                "Object",
                "An object",
                2.5d,
                10
        );

        itemRepository.addItem(newItem);

        assertTrue(itemRepository.contains(newItem));
    }
}