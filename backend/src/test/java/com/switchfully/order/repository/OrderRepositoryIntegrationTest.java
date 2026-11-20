package com.switchfully.order.repository;

import com.switchfully.order.IntegrationTest;
import com.switchfully.order.domain.Customer;
import com.switchfully.order.domain.Item;
import com.switchfully.order.domain.Order;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.List;

import static com.switchfully.order.domain.CustomerTestBuilder.aCustomer;
import static com.switchfully.order.domain.ItemTestBuilder.anItem;
import static com.switchfully.order.domain.OrderItemTestBuilder.anOrderItem;
import static com.switchfully.order.domain.OrderTestBuilder.anOrder;

class OrderRepositoryIntegrationTest extends IntegrationTest {

    @Autowired
    private OrderRepository repository;

    @Autowired
    private CustomerRepository customerRepository;

    @Autowired
    private ItemRepository itemRepository;

    private Customer customer;
    private Item item;

    @BeforeEach
    void createEntities() {
        customer = customerRepository.save(aCustomer().build());
        item = itemRepository.save(anItem().build());
    }

    @Test
    void save() {
        Order orderToSave = anOrder()
                .withCustomerId(customer.getId())
                .withOrderItems(anOrderItem().withItemId(item.getId()).build())
                .build();

        Order savedOrder = repository.save(orderToSave);

        Assertions.assertThat(savedOrder.getId()).isNotNull();
        Assertions.assertThat(repository.get(savedOrder.getId()))
                .usingRecursiveComparison()
                .isEqualTo(savedOrder);
    }

    @Test
    void get() {
        Order savedOrder = repository.save(anOrder()
                .withCustomerId(customer.getId())
                .withOrderItems(anOrderItem().withItemId(item.getId()).build())
                .build());

        Order actualOrder = repository.get(savedOrder.getId());

        Assertions.assertThat(actualOrder)
                .usingRecursiveComparison()
                .isEqualTo(savedOrder);
    }

    @Test
    void getAll() {
        Order orderOne = repository.save(anOrder()
                .withCustomerId(customer.getId())
                .withOrderItems(anOrderItem().withItemId(item.getId()).build())
                .build());
        Order orderTwo = repository.save(anOrder()
                .withCustomerId(customer.getId())
                .withOrderItems(anOrderItem().withItemId(item.getId()).build())
                .build());

        List<Order> allOrders = repository.getAll();

        Assertions.assertThat(allOrders)
                .containsExactlyInAnyOrder(orderOne, orderTwo);
    }
}
