package com.switchfully.eurder.eurder.service;

import com.switchfully.eurder.eurder.api.dtos.CreateCustomerDTO;
import com.switchfully.eurder.eurder.api.dtos.CustomerDTO;
import com.switchfully.eurder.eurder.domain.user.Customer;
import com.switchfully.eurder.eurder.repository.UserRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class CustomerServiceUnitTest {

    @Mock
    private UserRepository userRepository;

    @InjectMocks
    private CustomerService customerService;

    @Test
    void addCustomer_shouldSaveCustomerAndReturnCustomerDTO() {
        CreateCustomerDTO createCustomerDTO = new CreateCustomerDTO(
                "John",
                "Doe",
                "john@mail.com",
                "password123",
                "Brussels",
                "0471234567"
        );

        Customer savedCustomer = new Customer(
                "John",
                "Doe",
                "john@mail.com",
                "password123",
                "Brussels",
                "0471234567"
        );

        when(userRepository.save(any(Customer.class)))
                .thenReturn(savedCustomer);

        CustomerDTO result = customerService.addCustomer(createCustomerDTO);

        verify(userRepository).save(any(Customer.class));

        assertEquals("John", result.firstname());
        assertEquals("Doe", result.lastname());
        assertEquals("john@mail.com", result.email());
    }
}
