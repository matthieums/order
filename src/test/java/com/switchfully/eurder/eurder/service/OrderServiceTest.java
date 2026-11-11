package com.switchfully.eurder.eurder.service;

import com.switchfully.eurder.eurder.api.dtos.CreateItemGroupDTO;
import com.switchfully.eurder.eurder.api.dtos.CreateOrderDTO;
import com.switchfully.eurder.eurder.api.dtos.OrderDetailsDTO;
import com.switchfully.eurder.eurder.domain.Eurder;
import com.switchfully.eurder.eurder.domain.Item;
import com.switchfully.eurder.eurder.domain.ItemGroup;
import com.switchfully.eurder.eurder.domain.user.Customer;
import com.switchfully.eurder.eurder.domain.user.User;
import com.switchfully.eurder.eurder.exception.CustomerDoesNotExistException;
import com.switchfully.eurder.eurder.exception.ItemDoesNotExistException;
import com.switchfully.eurder.eurder.domain.ItemFactory;
import com.switchfully.eurder.eurder.exception.OrderDoesNotExistException;
import com.switchfully.eurder.eurder.factory.OrderFactory;
import com.switchfully.eurder.eurder.factory.UserFactory;
import com.switchfully.eurder.eurder.mapper.*;
import com.switchfully.eurder.eurder.repository.*;
import org.junit.jupiter.api.BeforeEach;
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
    private CustomerService customerService;
    @Mock
    private StockService stockService;
    @Mock
    private ItemGroupService itemGroupService;
    @Mock
    private OrderMapper orderMapper;
    @Mock
    private ReportMapper reportMapper;

    private OrderService orderService;

    @BeforeEach
    void setUp() {
        orderService = new OrderService(
                orderRepository,
                customerService,
                orderMapper,
                reportMapper,
                stockService,
                itemGroupService
        );
    }

    @Test
    void addOrder_shouldThrowException_whenCustomerDoesNotExist() {
        Long customerId = 1L;

        CreateOrderDTO dto = new CreateOrderDTO(
                List.of(),
                customerId
        );

        when(customerService.findById(customerId))
                .thenThrow(new CustomerDoesNotExistException(customerId));

        assertThrows(
                CustomerDoesNotExistException.class,
                () -> orderService.addOrder(dto)
        );
    }

    @Test
    void shouldProcessStockForEachItemGroup_whenAddingOrder() {
        Long customerId = 1L;
        Customer customer = UserFactory.customerWithValidDetails();
        Eurder order = new Eurder(customer);
        Item item = ItemFactory.itemWithInfiniteSupply();
        ItemGroup group1 = new ItemGroup(order, item, 1);
        ItemGroup group2 = new ItemGroup(order, item, 1);
        CreateItemGroupDTO createItemGroupDTO1 = new CreateItemGroupDTO(item.getId(), 1);
        CreateItemGroupDTO createItemGroupDTO2 = new CreateItemGroupDTO(item.getId(), 1);
        CreateOrderDTO createOrderDTO = new CreateOrderDTO(List.of(createItemGroupDTO1, createItemGroupDTO2), customerId);

        when(customerService.findById(customerId)).thenReturn(customer);

        when(itemGroupService.createItemGroups(any(), any()))
                .thenReturn(List.of(group1, group2));

        when(orderRepository.save(any())).thenReturn(order);

        when(itemGroupService.saveAll(anyList())).thenReturn(List.of(group1, group2));

        when(itemGroupService.calculateTotalPriceOfGroups(anyList())).thenReturn(BigDecimal.TEN);

        when(orderMapper.toDetailsDTO(any(), any(), any())).thenReturn(mock(OrderDetailsDTO.class));

        orderService.addOrder(createOrderDTO);

        verify(stockService).processItemGroupStock(group1);
        verify(stockService).processItemGroupStock(group2);
    }

    @Test
    void shouldThrowException_whenUserTriesToReorderAnotherCustomersOrder() {
        Long orderId = 1L;
        Long userId = 2L;
        Eurder order = new Eurder(UserFactory.customerWithValidDetails());
        when(orderRepository.findById(orderId))
                .thenReturn(Optional.of(order));

        assertThrows(OrderDoesNotExistException.class,
                () -> orderService.reorder(userId, orderId));
    }

    @Test
    void shouldCreateNewOrder_whenReorderingValidOrder() {
        Long orderId = 1L;
        Long userId = 2L;
        Customer customer = UserFactory.customerWithValidDetails();
        customer.setId(userId);
        Eurder order = new Eurder(customer);

        ItemGroup duplicatedGroup = new ItemGroup(order, ItemFactory.itemWithInfiniteSupply(), 1);

        when(orderRepository.findById(orderId)).thenReturn(Optional.of(order));
        when(itemGroupService.duplicateWithUpdatedPrices(eq(order), any()))
                .thenReturn(List.of(duplicatedGroup));
        when(orderRepository.save(any())).thenReturn(order);
        when(itemGroupService.saveAll(anyList())).thenReturn(List.of(duplicatedGroup));
        when(itemGroupService.calculateTotalPriceOfGroups(anyList())).thenReturn(BigDecimal.TEN);
        when(orderMapper.toDetailsDTO(any(), any(), any())).thenReturn(mock(OrderDetailsDTO.class));

        orderService.reorder(userId, orderId);

        verify(itemGroupService).duplicateWithUpdatedPrices(
                eq(order),
                argThat(newOrder -> !newOrder.equals(order))
        );

        verify(stockService).processItemGroupStock(duplicatedGroup);
    }
}
