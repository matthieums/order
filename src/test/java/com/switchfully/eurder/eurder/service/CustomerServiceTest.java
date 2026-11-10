package com.switchfully.eurder.eurder.service;

import com.switchfully.eurder.eurder.api.dtos.CreateCustomerDTO;
import com.switchfully.eurder.eurder.api.dtos.CustomerDTO;
import com.switchfully.eurder.eurder.domain.user.Customer;
import com.switchfully.eurder.eurder.factory.UserFactory;
import com.switchfully.eurder.eurder.mapper.CustomerMapper;
import com.switchfully.eurder.eurder.repository.CustomerRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;

import javax.swing.text.html.Option;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class CustomerServiceTest {
    @Mock
    private CustomerRepository customerRepository;

    @Mock
    private CustomerMapper customerMapper;

    @InjectMocks
    private CustomerService customerService;

    @Test
    void addCustomer_shouldSaveCustomerAndReturnCustomerDTO() {
        Customer customer = UserFactory.customerWithValidDetails();

        CreateCustomerDTO createCustomerDTO = new CreateCustomerDTO(
                customer.getFirstname(),
                customer.getLastname(),
                customer.getEmail(),
                customer.getAddress(),
                customer.getPhoneNumber(),
                customer.getPassword()
        );
        CustomerDTO expectedDTO = new CustomerDTO(
                customer.getId(),
                customer.getFirstname(),
                customer.getLastname(),
                customer.getEmail(),
                customer.getAddress(),
                customer.getPhoneNumber()
        );

        when(customerMapper.toObject(createCustomerDTO))
                .thenReturn(customer);

        when(customerRepository.save(customer))
                .thenReturn(customer);

        when(customerMapper.toDTO(customer))
                .thenReturn(expectedDTO);

        CustomerDTO result = customerService.addCustomer(createCustomerDTO);
        assertEquals(expectedDTO, result);

        verify(customerMapper).toObject(createCustomerDTO);
        verify(customerRepository).save(customer);
        verify(customerMapper).toDTO(customer);
    }

    @Test
    void get_shouldReturnCustomerDTO_whenCustomerExists() {
        Long customerId = 1L;
        Customer customer = UserFactory.customerWithValidDetails();
        CustomerDTO expectedDTO = new CustomerDTO(
                customerId,
                customer.getFirstname(),
                customer.getLastname(),
                customer.getEmail(),
                customer.getAddress(),
                customer.getPhoneNumber()
        );

        when(customerRepository.findById(customerId))
                .thenReturn(Optional.of(customer));

        when(customerMapper.toDTO(customer))
                .thenReturn(expectedDTO);

        CustomerDTO result = customerService.get(customerId);

        assertEquals(expectedDTO, result);
    }
}