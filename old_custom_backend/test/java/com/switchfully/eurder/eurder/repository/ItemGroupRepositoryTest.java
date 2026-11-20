package com.switchfully.eurder.eurder.repository;

import com.switchfully.eurder.eurder.domain.Eurder;
import com.switchfully.eurder.eurder.domain.Item;
import com.switchfully.eurder.eurder.domain.ItemGroup;
import com.switchfully.eurder.eurder.domain.user.Customer;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.data.jpa.test.autoconfigure.DataJpaTest;
import org.springframework.boot.jdbc.test.autoconfigure.AutoConfigureTestDatabase;
import org.springframework.test.context.ActiveProfiles;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

@DataJpaTest
@ActiveProfiles("test")
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
class ItemGroupRepositoryTest {

    @Autowired
    private ItemGroupRepository itemGroupRepository;

    @Autowired
    private OrderRepository orderRepository;

    @Autowired
    private ItemRepository itemRepository;

    @Autowired
    private UserRepository userRepository;

    @Test
    void shouldPersistAndRetrieveItemGroup() {
        ItemGroup itemGroup = createItemGroup();

        ItemGroup saved = itemGroupRepository.saveAndFlush(itemGroup);

        assertNotNull(saved.getId());

        Optional<ItemGroup> result =
                itemGroupRepository.findById(saved.getId());

        assertTrue(result.isPresent());
        assertEquals(2, result.get().getAmount());
        assertEquals(LocalDate.of(2026, 12, 1),
                result.get().getShippingDate());
    }

    @Test
    void shouldFindAllItemGroupsByEurderId() {
        Eurder eurder = createEurder();

        Item item1 = createItem("Keyboard");
        Item item2 = createItem("Mouse");

        ItemGroup group1 = createItemGroup(eurder, item1,
                LocalDate.of(2026, 12, 1));

        ItemGroup group2 = createItemGroup(eurder, item2,
                LocalDate.of(2026, 12, 2));

        itemGroupRepository.saveAllAndFlush(List.of(group1, group2));

        List<ItemGroup> result =
                itemGroupRepository.findAllByEurder_Id(eurder.getId());

        assertEquals(2, result.size());
        assertTrue(result.stream().allMatch(
                group -> group.getEurder().getId().equals(eurder.getId())
        ));
    }

    @Test
    void shouldReturnEmptyListWhenEurderHasNoItemGroups() {
        Eurder eurder = createEurder();

        List<ItemGroup> result =
                itemGroupRepository.findAllByEurder_Id(eurder.getId());

        assertTrue(result.isEmpty());
    }

    @Test
    void shouldFindAllItemGroupsByShippingDate() {
        LocalDate shippingDate = LocalDate.of(2026, 12, 1);

        Eurder eurder = createEurder();

        Item item1 = createItem("Keyboard");
        Item item2 = createItem("Mouse");
        Item item3 = createItem("Monitor");

        ItemGroup group1 = createItemGroup(eurder, item1, shippingDate);
        ItemGroup group2 = createItemGroup(eurder, item2, shippingDate);
        ItemGroup group3 = createItemGroup(
                eurder, item3, LocalDate.of(2026, 12, 2)
        );

        itemGroupRepository.saveAllAndFlush(
                List.of(group1, group2, group3)
        );

        List<ItemGroup> result =
                itemGroupRepository.findAllByShippingDate(shippingDate);

        assertEquals(2, result.size());

        assertTrue(result.stream().allMatch(
                group -> group.getShippingDate().equals(shippingDate)
        ));
    }

    @Test
    void shouldReturnEmptyListWhenNoItemGroupsMatchShippingDate() {
        List<ItemGroup> result =
                itemGroupRepository.findAllByShippingDate(
                        LocalDate.of(2030, 1, 1)
                );

        assertTrue(result.isEmpty());
    }

    private ItemGroup createItemGroup() {
        Eurder eurder = createEurder();
        Item item = createItem("Keyboard");

        return createItemGroup(
                eurder,
                item,
                LocalDate.of(2026, 12, 1)
        );
    }

    private ItemGroup createItemGroup(
            Eurder eurder,
            Item item,
            LocalDate shippingDate
    ) {
        ItemGroup group = new ItemGroup(
                eurder,
                item,
                2
        );
        group.setShippingDate(shippingDate);
        return group;
    }

    private Eurder createEurder() {
        Customer customer = new Customer(
                "John",
                "Doe",
                "john" + System.nanoTime() + "@test.com",
                "password",
                "Some address",
                "0412345678"
        );

        userRepository.saveAndFlush(customer);

        Eurder eurder = new Eurder(customer);

        return orderRepository.saveAndFlush(eurder);
    }

    private Item createItem(String name) {
        Item item = new Item(
                name,
                "Test description",
                new BigDecimal("49.99"),
                10
        );

        return itemRepository.saveAndFlush(item);
    }
}