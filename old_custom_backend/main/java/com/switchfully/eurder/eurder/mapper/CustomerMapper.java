package com.switchfully.eurder.eurder.mapper;

import com.switchfully.eurder.eurder.api.dtos.CreateCustomerDTO;
import com.switchfully.eurder.eurder.api.dtos.CustomerDTO;
import com.switchfully.eurder.eurder.domain.user.Customer;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class CustomerMapper {

    public CustomerDTO toDTO(Customer customer) {
        if (customer == null) return null;

        return new CustomerDTO(
                customer.getId(),
                customer.getFirstname(),
                customer.getLastname(),
                customer.getEmail(),
                customer.getAddress(),
                customer.getPhoneNumber()
        );
    }

    public List<CustomerDTO> toDTO(List<Customer> customers) {
        if (customers == null) return null;
        return customers.stream()
                .map(this::toDTO)
                .toList();
    }
    
    public Customer toObject(CreateCustomerDTO dto) {
        return new Customer(
                dto.firstname(),
                dto.lastname(),
                dto.email(),
                dto.password(),
                dto.address(),
                dto.phoneNumber()
        );
    }
}
