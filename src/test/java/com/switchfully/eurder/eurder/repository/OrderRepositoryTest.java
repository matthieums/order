package com.switchfully.eurder.eurder.repository;

import static org.junit.jupiter.api.Assertions.*;


import com.switchfully.eurder.eurder.domain.Eurder;
import com.switchfully.eurder.eurder.domain.user.Customer;
import org.junit.jupiter.api.Test;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.data.jpa.test.autoconfigure.DataJpaTest;
import org.springframework.boot.jdbc.test.autoconfigure.AutoConfigureTestDatabase;
import org.springframework.test.context.ActiveProfiles;

import java.util.List;
import java.util.Optional;

@DataJpaTest
@ActiveProfiles("test")
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
class OrderRepositoryTest {

    @Autowired
    private OrderRepository orderRepository;

    @Autowired
    private UserRepository userRepository;

    @Test
    void shouldSaveOrder() {
        Customer customer = createCustomer();

        Eurder eurder = new Eurder(customer);

        Eurder saved = orderRepository.saveAndFlush(eurder);

        assertNotNull(saved.getId());
    }

    @Test
    void shouldFindOrderById() {
        Customer customer = createCustomer();

        Eurder saved = orderRepository.saveAndFlush(
                new Eurder(customer)
        );

        Optional<Eurder> result =
                orderRepository.findById(saved.getId());

        assertTrue(result.isPresent());
        assertEquals(customer.getId(),
                result.get().getCustomer().getId());
    }

    @Test
    void shouldFindAllOrdersByCustomerId() {
        Customer customer = createCustomer();

        Eurder order1 = new Eurder(customer);
        Eurder order2 = new Eurder(customer);

        orderRepository.saveAllAndFlush(List.of(order1, order2));

        List<Eurder> result =
                orderRepository.findAllByCustomerId(customer.getId());

        assertEquals(2, result.size());

        assertTrue(result.stream().allMatch(
                order -> order.getCustomer().getId().equals(customer.getId())
        ));
    }

    @Test
    void shouldReturnEmptyListWhenCustomerHasNoOrders() {
        Customer customer = createCustomer();

        List<Eurder> result =
                orderRepository.findAllByCustomerId(customer.getId());

        assertTrue(result.isEmpty());
    }

    @Test
    void shouldNotReturnOrdersFromAnotherCustomer() {
        Customer customer1 = createCustomer();
        Customer customer2 = createCustomer();

        orderRepository.saveAndFlush(new Eurder(customer1));
        orderRepository.saveAndFlush(new Eurder(customer2));

        List<Eurder> result =
                orderRepository.findAllByCustomerId(customer1.getId());

        assertEquals(1, result.size());
        assertEquals(
                customer1.getId(),
                result.getFirst().getCustomer().getId()
        );
    }

    private Customer createCustomer() {
        Customer customer = new Customer(
                "John",
                "Doe",
                "john" + System.nanoTime() + "@test.com",
                "password",
                "Some address",
                "0412345678"
        );

        return userRepository.saveAndFlush(customer);
    }
}