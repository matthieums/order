package com.switchfully.eurder.eurder.mapper;

import com.switchfully.eurder.eurder.api.dtos.CreateCustomerDTO;
import com.switchfully.eurder.eurder.api.dtos.CustomerDTO;
import com.switchfully.eurder.eurder.domain.Customer;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

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
        CustomerDTO expected = new CustomerDTO(customer.firstname(), customer.lastname(), customer.email(), customer.address(), customer.phoneNumber());

        assertThat(CustomerMapper.toDTO(customer))
                .usingRecursiveComparison()
                .isEqualTo(expected);
    }

    @Test
    void givenACreateCustomerDTO_whenToMember_thenReturnAMemberWithAppropriateValues() {
        CreateCustomerDTO dto = new CreateCustomerDTO(
                "newUser",
                "user",
                "user@dto.com",
                "userAddress - 5555",
                "04 555 555 555"
        );

        assertThat(CustomerMapper.toObject(dto))
                .usingRecursiveComparison()
                .isEqualTo(
                        new Customer(
                                dto.getFirstname(),
                                dto.getLastname(),
                                dto.getEmail(),
                                dto.getAddress(),
                                dto.getPhoneNumber()
                        )
            );
    }
}