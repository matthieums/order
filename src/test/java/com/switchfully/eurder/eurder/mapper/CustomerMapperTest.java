package com.switchfully.eurder.eurder.mapper;

import com.switchfully.eurder.eurder.api.dtos.CreateCustomerDTO;
import com.switchfully.eurder.eurder.api.dtos.CustomerDTO;
import com.switchfully.eurder.eurder.domain.user.Customer;
import com.switchfully.eurder.eurder.factory.DTOFactory;
import com.switchfully.eurder.eurder.factory.UserFactory;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

class CustomerMapperTest {

    @Test
    void givenACustomerInstance_whenToDto_thenReturnCustomerDTO() {
        Customer customer = UserFactory.customerWithValidDetails();
        CustomerDTO expected = new CustomerDTO(
                customer.getFirstname(),
                customer.getLastname(),
                customer.getEmail(),
                customer.getAddress(),
                customer.getPhoneNumber()
        );

        assertThat(CustomerMapper.toDTO(customer))
                .usingRecursiveComparison()
                .isEqualTo(expected);
    }

    @Test
    void givenACreateCustomerDTO_whenToCustomer_thenReturnACustomerWithAppropriateValues() {
        CreateCustomerDTO dto = DTOFactory.validCreateCustomerDTO();

        assertThat(CustomerMapper.toObject(dto))
                .usingRecursiveComparison()
                .isEqualTo(
                        new Customer(
                                dto.getFirstname(),
                                dto.getLastname(),
                                dto.getEmail(),
                                dto.getPassword(),
                                dto.getAddress(),
                                dto.getPhoneNumber()
                        )
            );
    }
}