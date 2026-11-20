package com.switchfully.eurder.eurder.domain;

import com.switchfully.eurder.eurder.domain.user.Customer;
import com.switchfully.eurder.eurder.factory.UserFactory;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;

import static org.junit.jupiter.api.Assertions.*;

class ItemGroupTest {
    private Eurder eurder;
    private Item item;
    private ItemGroup itemGroup;

    @BeforeEach
    void setup() {
        Customer customer = UserFactory.customerWithValidDetails();
        eurder = new Eurder(customer);
        item = new Item(
                "Laptop",
                "Gaming laptop",
                new BigDecimal("1200.00"),
                10
        );
        itemGroup = new ItemGroup(eurder, item, 3);
    }

    @Test
    void givenItemGroup_whenCalculatingPrice_thenCorrectTotalIsReturned() {
        assertEquals(
                new BigDecimal("3600.00"),
                itemGroup.calculatePrice()
        );
    }
    @Test
    void givenItemPriceChanges_whenCalculatingGroupPrice_thenOriginalPriceIsUsed() {
        item.setPrice(new BigDecimal("1500.00"));

        assertEquals(
                new BigDecimal("3600.00"),
                itemGroup.calculatePrice()
        );
    }

    @Test
    void givenItemGroup_whenDuplicating_thenNewGroupHasSameProperties() {
        Eurder newOrder = new Eurder(eurder.getCustomer());

        ItemGroup duplicate = itemGroup.duplicateWithUpdatedItemPrice(newOrder);

        assertNotSame(itemGroup, duplicate);
        assertEquals(newOrder, duplicate.getEurder());
        assertEquals(item, duplicate.getItem());
        assertEquals(3, duplicate.getAmount());
        assertEquals(
                new BigDecimal("3600.00"),
                duplicate.calculatePrice()
        );
    }

    @Test
    void givenItemGroup_whenChangingItemPriceAndDuplicating_thenNewGroupHasUpdatedPrice() {
        Eurder newOrder = new Eurder(eurder.getCustomer());
        item.setPrice(new BigDecimal("1000.00"));

        ItemGroup duplicate = itemGroup.duplicateWithUpdatedItemPrice(newOrder);

        assertNotSame(itemGroup, duplicate);
        assertEquals(newOrder, duplicate.getEurder());
        assertEquals(item, duplicate.getItem());
        assertEquals(3, duplicate.getAmount());
        assertEquals(
                new BigDecimal("3000.00"),
                duplicate.calculatePrice()
        );
    }
}