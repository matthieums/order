package com.switchfully.eurder.eurder.mapper;

import com.switchfully.eurder.eurder.api.dtos.CreateCustomerDTO;
import com.switchfully.eurder.eurder.api.dtos.CustomerDTO;
import com.switchfully.eurder.eurder.domain.Customer;

public class CustomerMapper {

    public static CustomerDTO toDTO(Customer customer) {
        if (customer == null) return null;

        return new CustomerDTO(
                customer.getFirstname(),
                customer.getLastname()
        );
    }

    public static Customer toCustomer(CreateCustomerDTO dto) {
        return new Customer(
                dto.getFirstname(),
                dto.getLastname(),
                dto.getEmail(),
                dto.getAddress(),
                dto.getPhoneNumber()
        );
    }
}
