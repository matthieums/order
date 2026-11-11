package com.switchfully.eurder.eurder.service;

import com.switchfully.eurder.eurder.api.dtos.CreateItemGroupDTO;
import com.switchfully.eurder.eurder.domain.Eurder;
import com.switchfully.eurder.eurder.domain.Item;
import com.switchfully.eurder.eurder.domain.ItemFactory;
import com.switchfully.eurder.eurder.domain.ItemGroup;
import com.switchfully.eurder.eurder.domain.user.Customer;
import com.switchfully.eurder.eurder.exception.ItemDoesNotExistException;
import com.switchfully.eurder.eurder.factory.OrderFactory;
import com.switchfully.eurder.eurder.factory.UserFactory;
import com.switchfully.eurder.eurder.mapper.ItemGroupMapper;
import com.switchfully.eurder.eurder.repository.ItemRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class ItemGroupServiceTest {

    @Mock
    private ItemRepository itemRepository;

    @Mock
    private ItemGroupMapper itemGroupMapper;

    @Mock
    private ItemService itemService;

    @InjectMocks
    private ItemGroupService itemGroupService;

    @Test
    void createItemGroups_shouldReturnItemGroups_whenItemsExist() {
        Long itemId = 1L;

        Item item = ItemFactory.itemWithInfiniteSupply();
        Eurder order = OrderFactory.order();

        CreateItemGroupDTO dto = new CreateItemGroupDTO(
                itemId, 3
        );

        ItemGroup expectedGroup = new ItemGroup(order, item, 3);

        when(itemService.findById(itemId))
                .thenReturn(item);

        when(itemGroupMapper.toDomain(order, dto, item))
                .thenReturn(expectedGroup);

        List<ItemGroup> result = itemGroupService.createItemGroups(order, List.of(dto));

        assertEquals(1, result.size());
        assertEquals(expectedGroup, result.getFirst());

        verify(itemService).findById(itemId);
        verify(itemGroupMapper).toDomain(order, dto, item);
    }

    @Test
    void createItemGroups_shouldThrowException_whenItemDoesNotExist() {
        Long itemId = 1L;
        Eurder order = new Eurder(UserFactory.customerWithValidDetails());
        CreateItemGroupDTO dto = new CreateItemGroupDTO(
                itemId, 3
        );

        when(itemService.findById(itemId))
                .thenThrow(new ItemDoesNotExistException(itemId));

        assertThrows(
                ItemDoesNotExistException.class,
                () -> itemGroupService.createItemGroups(order, List.of(dto))
        );
        verify(itemService).findById(itemId);
    }

    @Test
    void createItemGroups_shouldAssociateItemGroupsWithGivenOrder() {
        Customer customer = UserFactory.customerWithValidDetails();
        Eurder order = new Eurder(customer);

        Long itemId = 1L;
        Item item = ItemFactory.itemWithInfiniteSupply();

        CreateItemGroupDTO dto = new CreateItemGroupDTO(
                itemId, 3
        );

        ItemGroup expectedGroup = new ItemGroup(order, item, 3);

        when(itemService.findById(itemId))
                .thenReturn(item);

        when(itemGroupMapper.toDomain(order, dto, item))
                .thenReturn(expectedGroup);

        List<ItemGroup> result = itemGroupService.createItemGroups(
                order, List.of(dto)
        );

        assertEquals(1, result.size());
        verify(itemGroupMapper).toDomain(order, dto, item);
    }
}
