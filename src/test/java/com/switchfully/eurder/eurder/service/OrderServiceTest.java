package com.switchfully.eurder.eurder.service;

import com.switchfully.eurder.eurder.api.dtos.CreateOrderDTO;
import com.switchfully.eurder.eurder.api.dtos.OrderDTO;
import com.switchfully.eurder.eurder.domain.user.Customer;
import com.switchfully.eurder.eurder.domain.Eurder;
import com.switchfully.eurder.eurder.domain.Item;
import com.switchfully.eurder.eurder.domain.ItemGroup;
import com.switchfully.eurder.eurder.factory.UserFactory;
import com.switchfully.eurder.eurder.repository.ItemRepository;
import com.switchfully.eurder.eurder.repository.OrderRepository;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.time.LocalDate;
import java.util.List;
import java.util.Map;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
class OrderServiceTest {
    @Autowired
    private OrderService orderService;
    @Autowired
    private OrderRepository orderRepository;
    @Autowired
    private ItemRepository itemRepository;

    private Customer customer;

    @BeforeEach
    void setup() {
        orderRepository.clear();
        itemRepository.clear();
        customer = UserFactory.customerWithValidDetails();
    }

    @Test
    void givenValidOrder_whenAddOrder_thenAddOrder() {
        Item item1 = new Item("item1", "an item", 5.0, 20);
        Item item2 = new Item("item2", "an item", 5.0, 20);

        itemRepository.addItems(List.of(item1, item2));
        CreateOrderDTO validCreateOrderDTO = new CreateOrderDTO(
                Map.of(
                        item1.getId(), item1.getAmount(),
                        item2.getId(), item2.getAmount()
                ),
                customer.getEmail()
        );

        OrderDTO order = orderService.addOrder(validCreateOrderDTO);

        Assertions.assertThat(orderRepository.getOrderById(order.id())).isNotNull();
    }

    @Test
    void givenOrderOfItemInStock_whenSetShippingDate_thenDateSetToDayPlus1() {
        Item item1 = new Item("item1", "an item", 5.0, 20);
        itemRepository.addItem(item1);
        CreateOrderDTO validCreateOrderDTO = new CreateOrderDTO(
                Map.of(
                        item1.getId(), item1.getAmount()
                ),
                customer.getEmail()
        );
        OrderDTO orderDTO = orderService.addOrder(validCreateOrderDTO);
        Eurder order = orderRepository.getOrderById(orderDTO.id());

        LocalDate expectedDate = LocalDate.now().plusDays(1);

        Assertions.assertThat(order.getItemGroups().getFirst().getShippingDate().equals(expectedDate));
    }

    @Test
    void givenOrderOfItemWithoutStock_whenSetShippingDate_thenDateSetToDayPlus7() {
        Item item1 = new Item("item1", "an item", 5.0, 0);
        itemRepository.addItem(item1);
        OrderDTO orderDTO = orderService.addOrder(new CreateOrderDTO(
                        Map.of(
                                item1.getId(), item1.getAmount()
                        ),
                        customer.getEmail()
                )
        );

        Eurder order = orderRepository.getOrderById(orderDTO.id());

        LocalDate expectedDate = LocalDate.now().plusDays(7);
        Assertions.assertThat(order.getItemGroups().getFirst().getShippingDate().equals(expectedDate));
    }

    @Test
    void givenOrderWithAllItemsInStock_whenSetShippingDate_thenSetAllDatesToDayPlus1() {
        Item item1 = new Item("item1", "an item", 5.0, 20);
        Item item2 = new Item("item2", "an item", 5.0, 20);
        itemRepository.addItem(item1);
        itemRepository.addItem(item2);

        OrderDTO order = orderService.addOrder(
                new CreateOrderDTO(
                        Map.of(
                                item1.getId(), item1.getAmount(),
                                item2.getId(), item2.getAmount()
                        ),
                        customer.getEmail())
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
                        Map.of(
                                unavailableItem.getId(), 1
                        ),
                        customer.getEmail())
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