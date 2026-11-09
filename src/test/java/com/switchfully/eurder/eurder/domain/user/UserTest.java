package com.switchfully.eurder.eurder.domain.user;

import com.switchfully.eurder.eurder.factory.UserFactory;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class UserTest {
    private User admin;
    private User customer;

    @BeforeEach
    void setUp() {
        admin = UserFactory.admin();
        customer = UserFactory.customerWithValidDetails();
    }

    @Test
    void givenAdmin_whenCheckingAdminFeature_thenReturnsTrue() {
        assertTrue(admin.allowedToPerform(Feature.ADD_ITEM));
        assertTrue(admin.allowedToPerform(Feature.UPDATE_ITEM));
        assertTrue(admin.allowedToPerform(Feature.VIEW_ALL_CUSTOMERS));
        assertTrue(admin.allowedToPerform(Feature.VIEW_CUSTOMER_DETAILS));
        assertTrue(admin.allowedToPerform(Feature.GET_ORDER_OF_ITEM_WITH_SHIPPING_DATE));
    }

    @Test
    void givenCustomer_whenCheckingCustomerFeature_thenReturnsTrue() {
        assertTrue(customer.allowedToPerform(Feature.REORDER));
        assertTrue(customer.allowedToPerform(Feature.VIEW_REPORT));
    }

    @Test
    void givenAdmin_whenCheckingCustomerFeature_thenReturnsFalse() {
        assertFalse(admin.allowedToPerform(Feature.REORDER));
        assertFalse(admin.allowedToPerform(Feature.VIEW_REPORT));
    }

    @Test
    void givenCustomer_whenCheckingAdminFeature_thenReturnsFalse() {
        assertFalse(customer.allowedToPerform(Feature.ADD_ITEM));
        assertFalse(customer.allowedToPerform(Feature.UPDATE_ITEM));
        assertFalse(customer.allowedToPerform(Feature.VIEW_ALL_CUSTOMERS));
        assertFalse(customer.allowedToPerform(Feature.VIEW_CUSTOMER_DETAILS));
        assertFalse(customer.allowedToPerform(Feature.GET_ORDER_OF_ITEM_WITH_SHIPPING_DATE));
    }
}