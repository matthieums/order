package com.switchfully.eurder.eurder.repository;
import com.switchfully.eurder.eurder.domain.user.Admin;
import com.switchfully.eurder.eurder.domain.user.Customer;
import com.switchfully.eurder.eurder.domain.user.User;
import com.switchfully.eurder.eurder.factory.UserFactory;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.data.jpa.test.autoconfigure.DataJpaTest;
import org.springframework.boot.jdbc.test.autoconfigure.AutoConfigureTestDatabase;
import org.springframework.test.context.ActiveProfiles;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;


@DataJpaTest
@ActiveProfiles("test")
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
class UserRepositoryTest {
    @Autowired
    private UserRepository userRepository;

    @Test
    void shouldSaveCustomer() {
        Customer customer = UserFactory.customerWithValidDetails();

        User saved = userRepository.saveAndFlush(customer);

        assertNotNull(saved.getId());
        assertInstanceOf(Customer.class, saved);
    }
    @Test
    void shouldSaveAdmin() {
        Admin admin = new Admin(
                "Jane",
                "Smith",
                "admin@test.com",
                "password"
        );

        User saved = userRepository.saveAndFlush(admin);

        assertNotNull(saved.getId());
        assertInstanceOf(Admin.class, saved);
    }

    @Test
    void shouldFindUserById() {
        User saved = userRepository.saveAndFlush(createCustomer());

        Optional<User> result = userRepository.findById(saved.getId());

        assertTrue(result.isPresent());
        assertEquals("john@test.com", result.get().getEmail());
    }

    @Test
    void shouldFindCustomerByEmail() {
        Customer customer = createCustomer();
        userRepository.saveAndFlush(customer);

        User found = userRepository.findByEmail("john@test.com");

        assertNotNull(found);
        assertInstanceOf(Customer.class, found);
        assertEquals("John", found.getFirstname());
        assertEquals("Doe", found.getLastname());
    }

    @Test
    void shouldFindAdminByEmail() {
        Admin admin = new Admin(
                "Jane",
                "Smith",
                "admin@test.com",
                "password"
        );

        userRepository.saveAndFlush(admin);

        User found = userRepository.findByEmail("admin@test.com");

        assertNotNull(found);
        assertInstanceOf(Admin.class, found);
        assertEquals("Jane", found.getFirstname());
    }

    @Test
    void shouldReturnNullWhenEmailDoesNotExist() {
        User found = userRepository.findByEmail("unknown@test.com");

        assertNull(found);
    }

    @Test
    void shouldReturnEmptyWhenUserIdDoesNotExist() {
        Optional<User> result = userRepository.findById(999999L);

        assertTrue(result.isEmpty());
    }

    private Customer createCustomer() {
        return new Customer(
                "John",
                "Doe",
                "john@test.com",
                "password",
                "Some address",
                "0412345678"
        );
    }

}