package com.switchfully.eurder.eurder.service;

import com.switchfully.eurder.eurder.api.dtos.CreateItemGroupDTO;
import com.switchfully.eurder.eurder.api.dtos.CreateOrderDTO;
import com.switchfully.eurder.eurder.api.dtos.OrderDetailsDTO;
import com.switchfully.eurder.eurder.domain.Eurder;
import com.switchfully.eurder.eurder.domain.Item;
import com.switchfully.eurder.eurder.domain.ItemGroup;
import com.switchfully.eurder.eurder.domain.user.Customer;
import com.switchfully.eurder.eurder.exception.CustomerDoesNotExistException;
import com.switchfully.eurder.eurder.exception.ItemDoesNotExistException;
import com.switchfully.eurder.eurder.domain.ItemFactory;
import com.switchfully.eurder.eurder.factory.OrderFactory;
import com.switchfully.eurder.eurder.factory.UserFactory;
import com.switchfully.eurder.eurder.mapper.*;
import com.switchfully.eurder.eurder.repository.*;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;

@ExtendWith(MockitoExtension.class)
class OrderServiceTest {

    @Mock
    private OrderRepository orderRepository;

    @Mock
    private ItemRepository itemRepository;

    @Mock
    private CustomerRepository customerRepository;

    @Mock
    private ItemGroupRepository itemGroupRepository;

    @Mock
    private OrderMapper orderMapper;

    @Mock
    private ReportMapper reportMapper;

    @Mock
    private ItemGroupMapper itemGroupMapper;

    @InjectMocks
    private OrderService orderService;

    @Test
    void createItemGroups_shouldReturnItemGroups_whenItemsAndOrdersExist() {
        Long itemId = 1L;
        Long orderId = 2L;

        Item item = ItemFactory.itemWithInfiniteSupply();
        Eurder order = OrderFactory.order();

        CreateItemGroupDTO dto = new CreateItemGroupDTO(
                itemId, 3
        );

        ItemGroup expectedGroup = new ItemGroup(order, item, 3);

        when(itemRepository.findById(itemId))
                .thenReturn(Optional.of(item));

        when(itemGroupMapper.toDomain(order, dto, item))
                .thenReturn(expectedGroup);

        List<ItemGroup> result =
                orderService.createItemGroups(order, List.of(dto));

        assertEquals(1, result.size());
        assertEquals(expectedGroup, result.getFirst());

        verify(itemRepository).findById(itemId);
        verify(itemGroupMapper).toDomain(order, dto, item);
    }

    @Test
    void createItemGroups_shouldThrowException_whenItemDoesNotExist() {
        Long itemId = 1L;
        Eurder order = new Eurder(UserFactory.customerWithValidDetails());
        CreateItemGroupDTO dto = new CreateItemGroupDTO(
                itemId, 3
        );

        when(itemRepository.findById(itemId))
                .thenReturn(Optional.empty());

        assertThrows(
                ItemDoesNotExistException.class,
                () -> orderService.createItemGroups(order, List.of(dto))
        );

        verify(itemRepository).findById(itemId);
        verifyNoInteractions(orderRepository, itemGroupMapper);
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

        when(itemRepository.findById(itemId))
                .thenReturn(Optional.of(item));

        when(itemGroupMapper.toDomain(order, dto, item))
                .thenReturn(expectedGroup);

        List<ItemGroup> result = orderService.createItemGroups(
                order, List.of(dto)
        );

        assertEquals(1, result.size());
        assertSame(order, result.getFirst().getEurder());

        verify(itemRepository).findById(itemId);
        verify(itemGroupMapper).toDomain(order, dto, item);
        verifyNoInteractions(orderRepository);
    }

    @Test
    void addOrder_shouldThrowException_whenCustomerDoesNotExist() {
        Long customerId = 1L;

        CreateOrderDTO dto = new CreateOrderDTO(
                List.of(),
                customerId
        );

        when(customerRepository.findById(customerId))
                .thenReturn(Optional.empty());

        assertThrows(
                CustomerDoesNotExistException.class,
                () -> orderService.addOrder(dto)
        );

        verify(customerRepository).findById(customerId);
        verifyNoInteractions(orderRepository, itemGroupRepository);
    }

    @Test
    void addOrder_shouldReduceStock_whenItemIsImmediatelyAvailable() {
        Long customerId = 1L;
        Long itemId = 2L;

        Customer customer = UserFactory.customerWithValidDetails();
        Item item = ItemFactory.itemWithAmount(10);

        CreateItemGroupDTO groupDTO = new CreateItemGroupDTO(itemId, 2);

        CreateOrderDTO orderDTO = new CreateOrderDTO(
                List.of(groupDTO),
                customerId
        );

        OrderDetailsDTO expectedDTO = new OrderDetailsDTO(
                null,
                List.of(),
                new BigDecimal("2000.00")
        );

        when(orderMapper.toDetailsDTO(
                any(Eurder.class),
                anyList(),
                any(BigDecimal.class)
        )).thenReturn(expectedDTO);

        when(itemGroupMapper.toDomain(
                any(Eurder.class),
                eq(groupDTO),
                eq(item)
        )).thenReturn(new ItemGroup(
                new Eurder(customer),
                item,
                2
        ));

        when(customerRepository.findById(customerId))
                .thenReturn(Optional.of(customer));

        when(itemRepository.findById(itemId))
                .thenReturn(Optional.of(item));

        when(orderRepository.save(any(Eurder.class)))
                .thenAnswer(invocation -> invocation.getArgument(0));

        when(itemGroupRepository.saveAll(anyList()))
                .thenAnswer(invocation -> invocation.getArgument(0));

        OrderDetailsDTO result = orderService.addOrder(orderDTO);

        assertNotNull(result);
        assertEquals(8, item.getAmount());
    }
}
