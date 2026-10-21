package com.switchfully.eurder.eurder.mapper;

import com.switchfully.eurder.eurder.api.dtos.CreateCustomerDTO;
import com.switchfully.eurder.eurder.api.dtos.CustomerDTO;
import com.switchfully.eurder.eurder.domain.Customer;

import java.util.List;

public class CustomerMapper {

    public static CustomerDTO toDTO(Customer customer) {
        if (customer == null) return null;

        return new CustomerDTO(
                customer.firstname(),
                customer.lastname(),
                customer.email(),
                customer.address(),
                customer.phoneNumber()
        );
    }

    public static List<CustomerDTO> toDTO(List<Customer> customers) {
        return customers.stream()
                .map(CustomerMapper::toDTO)
                .toList();
    }

    public static Customer toObject(CreateCustomerDTO dto) {
        return new Customer(
                dto.getFirstname(),
                dto.getLastname(),
                dto.getEmail(),
                dto.getAddress(),
                dto.getPhoneNumber()
        );
    }
}
