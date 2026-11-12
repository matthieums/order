package com.switchfully.eurder.eurder.service;

import com.switchfully.eurder.eurder.api.dtos.CreateItemDTO;
import com.switchfully.eurder.eurder.api.dtos.ItemDTO;
import com.switchfully.eurder.eurder.domain.Item;
import com.switchfully.eurder.eurder.domain.UrgencyLevel;
import com.switchfully.eurder.eurder.exception.ItemDoesNotExistException;
import com.switchfully.eurder.eurder.domain.ItemFactory;
import com.switchfully.eurder.eurder.mapper.ItemMapper;
import com.switchfully.eurder.eurder.repository.ItemRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class ItemServiceTest {

    @Mock
    private ItemRepository itemRepository;

    @Mock
    private ItemMapper itemMapper;

    @InjectMocks
    private ItemService itemService;


    @Test
    void addItem_shouldSaveItemAndReturnItemDTO() {
        Item savedItem = ItemFactory.itemWithInfiniteSupply();
        CreateItemDTO createItemDTO = new CreateItemDTO(
                savedItem.getName(),
                savedItem.getDescription(),
                savedItem.getPrice(),
                savedItem.getAmount()
        );

        ItemDTO expectedDTO = new ItemDTO(
                savedItem.getId(),
                savedItem.getName(),
                savedItem.getDescription(),
                savedItem.getPrice()
        );

        when(itemRepository.save(any(Item.class)))
                .thenReturn(savedItem);

        when(itemMapper.toObject(createItemDTO))
                .thenReturn(savedItem);

        when(itemMapper.toDTO(savedItem))
                .thenReturn(expectedDTO);

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

        Item expectedItem = new Item(dto.name(), dto.description(), dto.price(), dto.amount());

        when(itemMapper.toObject(dto))
                .thenReturn(expectedItem);

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

        ItemDTO expectedDTO = new ItemDTO(
                existingItem.getId(),
                updateDTO.name(),
                updateDTO.description(),
                updateDTO.price()
        );

        when(itemRepository.findById(itemId))
                .thenReturn(Optional.of(existingItem));

        when(itemRepository.save(existingItem))
                .thenReturn(existingItem);

        when(itemMapper.toDTO(existingItem))
                .thenReturn(expectedDTO);

        ItemDTO result = itemService.updateItem(itemId, updateDTO);

        assertEquals(expectedDTO, result);
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

    @Test
    void getOverview_whenNoUrgency_shouldReturnAllItemsOrderedByAscendingOrder() {
        Item low = ItemFactory.itemWithAmount(3);
        Item medium = ItemFactory.itemWithAmount(7);
        Item high = ItemFactory.itemWithAmount(15);

        ItemDTO lowDTO = new ItemDTO(low.getId(), low.getName(), low.getDescription(), low.getPrice());
        ItemDTO mediumDTO = new ItemDTO(medium.getId(), medium.getName(), medium.getDescription(), medium.getPrice());
        ItemDTO highDTO = new ItemDTO(high.getId(), high.getName(), high.getDescription(), high.getPrice());

        when(itemRepository.findAll())
                .thenReturn(List.of(high, low, medium));

        when(itemMapper.toDTO(low)).thenReturn(lowDTO);
        when(itemMapper.toDTO(medium)).thenReturn(mediumDTO);
        when(itemMapper.toDTO(high)).thenReturn(highDTO);

        List<ItemDTO> result = itemService.getOverview(null);

        assertEquals(3, result.size());
        assertEquals(
                List.of(lowDTO, mediumDTO, highDTO),
                result
        );
    }

    @Test
    void getOverview_givenLowUrgency_shouldReturnLowStockItems() {
        Item low = ItemFactory.itemWithAmount(3);
        Item medium = ItemFactory.itemWithAmount(7);
        Item high = ItemFactory.itemWithAmount(15);

        ItemDTO lowDTO = new ItemDTO(
                low.getId(), low.getName(), low.getDescription(), low.getPrice());

        when(itemRepository.findAll())
                .thenReturn(List.of(high, low, medium));

        when(itemMapper.toDTO(low)).thenReturn(lowDTO);

        List<ItemDTO> result = itemService.getOverview(UrgencyLevel.STOCK_LOW);

        assertEquals(List.of(lowDTO), result);
    }
    @Test
    void getOverview_givenMediumUrgency_shouldReturnMediumStockItems() {
        Item low = ItemFactory.itemWithAmount(3);
        Item medium = ItemFactory.itemWithAmount(7);
        Item high = ItemFactory.itemWithAmount(15);

        ItemDTO mediumDTO = new ItemDTO(
                medium.getId(), medium.getName(), medium.getDescription(), medium.getPrice());

        when(itemRepository.findAll())
                .thenReturn(List.of(high, low, medium));

        when(itemMapper.toDTO(medium)).thenReturn(mediumDTO);

        List<ItemDTO> result = itemService.getOverview(UrgencyLevel.STOCK_MEDIUM);

        assertEquals(List.of(mediumDTO), result);
    }

    @Test
    void getOverview_givenHighUrgency_shouldReturnHighStockItems() {
        Item low = ItemFactory.itemWithAmount(3);
        Item medium = ItemFactory.itemWithAmount(7);
        Item high = ItemFactory.itemWithAmount(15);

        ItemDTO highDTO = new ItemDTO(
                high.getId(), high.getName(), high.getDescription(), high.getPrice());

        when(itemRepository.findAll())
                .thenReturn(List.of(high, low, medium));

        when(itemMapper.toDTO(high)).thenReturn(highDTO);

        List<ItemDTO> result = itemService.getOverview(UrgencyLevel.STOCK_HIGH);

        assertEquals(List.of(highDTO), result);
    }
}