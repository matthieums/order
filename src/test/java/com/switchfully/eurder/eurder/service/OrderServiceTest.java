package com.switchfully.eurder.eurder.service;

import com.switchfully.eurder.eurder.api.dtos.CreateOrderDTO;
import com.switchfully.eurder.eurder.api.dtos.OrderDTO;
import com.switchfully.eurder.eurder.domain.Customer;
import com.switchfully.eurder.eurder.domain.Eurder;
import com.switchfully.eurder.eurder.domain.Item;
import com.switchfully.eurder.eurder.domain.ItemGroup;
import com.switchfully.eurder.eurder.mapper.OrderMapper;
import com.switchfully.eurder.eurder.repository.ItemRepository;
import com.switchfully.eurder.eurder.repository.OrderRepository;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.time.LocalDate;
import java.util.List;

class OrderServiceTest {
    private OrderService orderService;
    private OrderRepository orderRepository;
    private ItemRepository itemRepository;
    private Customer customer;

    @BeforeEach
    void setup() {
        orderRepository = new OrderRepository();
        itemRepository = new ItemRepository();
        orderService = new OrderService(orderRepository, itemRepository);

        customer = new Customer(
                "newUser",
                "user",
                "user@dto.com",
                "userAddress - 5555",
                "04 555 555 555"
        );
    }

    @Test
    void givenValidOrder_whenAddOrder_thenAddOrder() {
        Item item1 = new Item("item1", "an item", 5.0, 20);
        Item item2 = new Item("item2", "an item", 5.0, 20);

        CreateOrderDTO validCreateOrderDTO = new CreateOrderDTO(
                List.of(
                        new ItemGroup(item1.getId(), 1),
                        new ItemGroup(item2.getId(), 1)
                ),
                customer.email()
        );

        OrderDTO order = orderService.addOrder(validCreateOrderDTO);

        Assertions.assertThat(orderRepository.getOrderById(order.id())).isNotNull();
    }

    @Test
    void givenOrderOfItemInStock_whenSetShippingDate_thenDateSetToDayPlus1() {
        Item item1 = new Item("item1", "an item", 5.0, 20);
        itemRepository.addItem(item1);
        CreateOrderDTO validCreateOrderDTO = new CreateOrderDTO(
                List.of(
                        new ItemGroup(item1.getId(), 1)
                ),
                customer.email()
        );
        OrderDTO order = orderService.addOrder(validCreateOrderDTO);

        LocalDate expectedDate = LocalDate.now().plusDays(1);

        Assertions.assertThat(order.items().getFirst().getShippingDate().equals(expectedDate));

    }

    @Test
    void givenOrderOfItemWithoutStock_whenSetShippingDate_thenDateSetToDayPlus7() {
        Item item1 = new Item("item1", "an item", 5.0, 0);
        itemRepository.addItem(item1);
        OrderDTO order = orderService.addOrder(new CreateOrderDTO(
                        List.of(
                                new ItemGroup(item1.getId(), 1)
                        ),
                        customer.email()
                )
        );

        LocalDate expectedDate = LocalDate.now().plusDays(7);
        Assertions.assertThat(order.items().getFirst().getShippingDate().equals(expectedDate));

    }

    @Test
    void givenOrderWithAllItemsInStock_whenSetShippingDate_thenSetAllDatesToDayPlus1() {
        Item item1 = new Item("item1", "an item", 5.0, 20);
        Item item2 = new Item("item2", "an item", 5.0, 20);
        itemRepository.addItem(item1);
        itemRepository.addItem(item2);

        OrderDTO order = orderService.addOrder(
                new CreateOrderDTO(
                        List.of(new ItemGroup(item1.getId(), 5),
                                new ItemGroup(item2.getId(), 5))
                        , customer.email())
        );

        List<ItemGroup> itemGroups = orderRepository.getOrderById(order.id()).getItemGroups();

        LocalDate expectedDate = LocalDate.now().plusDays(1);

        for (ItemGroup itemGroup : itemGroups) {
            Assertions.assertThat(itemGroup.getShippingDate())
                    .isEqualTo(expectedDate);
        }
    }

    @Test
    void givenOrderWithNoItemsInStock_whenSetShippingDate_thenSetAllDatesToDayPlus7() {
        Item unavailableItem = new Item("Unavailable item", "an item", 5.0, 0);
        itemRepository.addItem(unavailableItem);

        OrderDTO order = orderService.addOrder(
                new CreateOrderDTO(
                        List.of(new ItemGroup(unavailableItem.getId(), 1)),
                        customer.email())
        );

        List<ItemGroup> itemGroups =
                orderRepository.getOrderById(order.id()).getItemGroups();

        LocalDate expectedDate = LocalDate.now().plusDays(7);

        for (ItemGroup itemGroup : itemGroups) {
            Assertions.assertThat(itemGroup.getShippingDate())
                    .isEqualTo(expectedDate);
        }
    }
}