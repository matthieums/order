package com.switchfully.eurder.eurder.domain;

import com.switchfully.eurder.eurder.domain.user.Customer;
import com.switchfully.eurder.eurder.factory.UserFactory;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class EurderTest {
    private Customer customer;

    @BeforeEach
    void setup() {
        customer = UserFactory.customerWithValidDetails();
    }

    @Test
    void givenEurder_whenDuplicating_thenNewEurderHasSameCustomer() {
        Eurder original = new Eurder(customer);

        Eurder duplicate = original.duplicate();

        assertNotSame(original, duplicate);
        assertEquals(original.getCustomer(), duplicate.getCustomer());
    }
}