package com.switchfully.eurder.eurder.mapper;

import com.switchfully.eurder.eurder.api.dtos.CustomerDTO;
import com.switchfully.eurder.eurder.domain.Customer;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.*;

class CustomerMapperTest {

    @Test
    void givenACustomerInstance_whenToDto_thenReturnCustomerDTO() {
        Customer customer = new Customer(
                "Matthieu",
                "Martens",
                "user@user.com",
                "userAddress - 5555",
                "04 555 555 555"
        );

        CustomerDTO expected = new CustomerDTO(customer.getFirstname(), customer.getLastname());

        assertThat(CustomerMapper.toDTO(customer))
                .usingRecursiveComparison()
                .isEqualTo(expected);
    }
}