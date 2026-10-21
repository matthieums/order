package com.switchfully.eurder.eurder.repository;

import com.switchfully.eurder.eurder.domain.user.Customer;
import com.switchfully.eurder.eurder.factory.UserFactory;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class UserRepositoryTest {

    @Test
    void givenACustomer_whenAdd_thenCustomerExistsInRepository() {
        UserRepository userRepository = new UserRepository();
        Customer newCustomer = UserFactory.customerWithValidDetails();

        userRepository.addCustomer(newCustomer);

        assertTrue(userRepository.contains(newCustomer));
    }
}