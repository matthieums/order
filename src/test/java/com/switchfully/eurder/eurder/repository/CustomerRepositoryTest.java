package com.switchfully.eurder.eurder.repository;

import com.switchfully.eurder.eurder.domain.Customer;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class CustomerRepositoryTest {

    @Test
    void givenACustomer_whenAdd_thenCustomerExistsInRepository() {
        CustomerRepository customerRepository = new CustomerRepository();
        Customer newCustomer = new Customer(
                "Matthieu",
                "Martens",
                "user@user.com",
                "userAddress - 5555",
                "04 555 555 555"
        );

        customerRepository.addCustomer(newCustomer);

        assertTrue(customerRepository.exists(newCustomer));
    }
}