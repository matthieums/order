package com.switchfully.eurder.eurder.service;

import com.switchfully.eurder.eurder.api.dtos.CreateItemDTO;
import com.switchfully.eurder.eurder.api.dtos.ItemDTO;
import com.switchfully.eurder.eurder.domain.Item;
import com.switchfully.eurder.eurder.exception.ItemDoesNotExistException;
import com.switchfully.eurder.eurder.domain.ItemFactory;
import com.switchfully.eurder.eurder.mapper.ItemMapper;
import com.switchfully.eurder.eurder.repository.ItemRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.math.BigDecimal;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class ItemServiceTest {

    @Mock
    private ItemRepository itemRepository;

    private ItemService itemService;

    @BeforeEach
    void setUp() {
        itemService = new ItemService(itemRepository, new ItemMapper());
}
    @Test
    void addItem_shouldSaveItemAndReturnItemDTO() {
        Item savedItem = ItemFactory.itemWithInfiniteSupply();
        CreateItemDTO createItemDTO = new CreateItemDTO(
                savedItem.getName(),
                savedItem.getDescription(),
                savedItem.getPrice(),
                savedItem.getAmount()
        );


        when(itemRepository.save(any(Item.class)))
                .thenReturn(savedItem);

        ItemDTO result = itemService.addItem(createItemDTO);
        assertEquals(savedItem.getName(), result.name());
        assertEquals(savedItem.getDescription(), result.description());
        assertEquals(savedItem.getPrice(), result.price());

        verify(itemRepository).save(any(Item.class));
    }

    @Test
    void addItem_shouldSaveItemWithCorrectDetails() {
        CreateItemDTO dto = new CreateItemDTO(
                "Laptop",
                "Gaming laptop",
                new BigDecimal("1200.00"),
                10
        );

        when(itemRepository.save(any(Item.class)))
                .thenAnswer(invocation -> invocation.getArgument(0));

        itemService.addItem(dto);

        ArgumentCaptor<Item> itemCaptor = ArgumentCaptor.forClass(Item.class);

        verify(itemRepository).save(itemCaptor.capture());

        Item savedItem = itemCaptor.getValue();

        assertEquals(dto.name(), savedItem.getName());
        assertEquals(dto.description(), savedItem.getDescription());
        assertEquals(dto.price(), savedItem.getPrice());
        assertEquals(dto.amount(), savedItem.getAmount());
    }

    @Test
    void updateItem_shouldUpdateItem_whenItemExists() {
        Long itemId = 1L;

        Item existingItem = ItemFactory.itemWithInfiniteSupply();

        CreateItemDTO updateDTO = new CreateItemDTO(
                "Updated laptop",
                "Updated description",
                new BigDecimal("1500.00"),
                20
        );

        when(itemRepository.findById(itemId))
                .thenReturn(Optional.of(existingItem));

        when(itemRepository.save(existingItem))
                .thenReturn(existingItem);

        ItemDTO result = itemService.updateItem(itemId, updateDTO);

        assertEquals(updateDTO.name(), result.name());
        assertEquals(updateDTO.description(), result.description());
        assertEquals(updateDTO.price(), result.price());

        verify(itemRepository).findById(itemId);
        verify(itemRepository).save(existingItem);
    }

    @Test
    void updateItem_shouldThrowException_whenItemDoesNotExist() {
        Long itemId = 1L;

        CreateItemDTO updateDTO = new CreateItemDTO(
                "Updated laptop",
                "Updated description",
                new BigDecimal("1500.00"),
                20
        );

        when(itemRepository.findById(itemId))
                .thenReturn(Optional.empty());

        assertThrows(
                ItemDoesNotExistException.class,
                () -> itemService.updateItem(itemId, updateDTO)
        );

        verify(itemRepository).findById(itemId);
        verify(itemRepository, never()).save(any(Item.class));
    }
}