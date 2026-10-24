package com.switchfully.eurder.eurder.repository;

import com.switchfully.eurder.eurder.domain.Item;
import com.switchfully.eurder.eurder.factory.ItemFactory;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
class ItemRepositoryTest {
    @Autowired
    private ItemRepository itemRepository;

    @BeforeEach
    void setup() {
    }

    @Test
    void givenAnItem_whenAdd_thenItemExistsInRepository(){
        Item newItem = ItemFactory.itemWithInfiniteSupply();

        itemRepository.addItem(newItem);

        assertTrue(itemRepository.contains(newItem));
    }
}