
package com.switchfully.eurder.eurder.mapper;

import com.switchfully.eurder.eurder.api.dtos.CreateCustomerDTO;
import com.switchfully.eurder.eurder.api.dtos.CustomerDTO;
import com.switchfully.eurder.eurder.domain.user.Customer;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class CustomerMapperTest {

    private CustomerMapper customerMapper;

    @BeforeEach
    void setUp() {
        customerMapper = new CustomerMapper();
    }

    @Test
    void toDTO_shouldMapCustomerCorrectly() {
        Customer customer = new Customer(
                "John", "Doe", "john@test.com",
                "password", "Main Street 1", "123456789"
        );

        CustomerDTO result = customerMapper.toDTO(customer);

        assertEquals(customer.getFirstname(), result.firstname());
        assertEquals(customer.getLastname(), result.lastname());
        assertEquals(customer.getEmail(), result.email());
        assertEquals(customer.getAddress(), result.address());
        assertEquals(customer.getPhoneNumber(), result.phoneNumber());
    }

    @Test
    void toDTO_shouldReturnNull_whenCustomerIsNull() {
        assertNull(customerMapper.toDTO((Customer) null));
    }

    @Test
    void toDTO_shouldMapListOfCustomers() {
        Customer customer1 = new Customer(
                "John", "Doe", "john@test.com",
                "password", "Street 1", "111"
        );

        Customer customer2 = new Customer(
                "Jane", "Doe", "jane@test.com",
                "password", "Street 2", "222"
        );

        List<CustomerDTO> result =
                customerMapper.toDTO(List.of(customer1, customer2));

        assertEquals(2, result.size());
        assertEquals(customer1.getFirstname(), result.get(0).firstname());
        assertEquals(customer2.getFirstname(), result.get(1).firstname());
    }

    @Test
    void toDTO_shouldReturnNull_whenCustomerListIsNull() {
        assertNull(customerMapper.toDTO((List<Customer>) null));
    }

    @Test
    void toObject_shouldConvertCreateCustomerDTOToCustomer() {
        CreateCustomerDTO dto = new CreateCustomerDTO(
                "John", "Doe", "john@test.com",
                "Main Street 1", "123456789", "password"
        );

        Customer result = customerMapper.toObject(dto);

        assertEquals(dto.firstname(), result.getFirstname());
        assertEquals(dto.lastname(), result.getLastname());
        assertEquals(dto.email(), result.getEmail());
        assertEquals(dto.password(), result.getPassword());
        assertEquals(dto.address(), result.getAddress());
        assertEquals(dto.phoneNumber(), result.getPhoneNumber());
    }
}
