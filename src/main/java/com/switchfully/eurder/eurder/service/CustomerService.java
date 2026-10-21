package com.switchfully.eurder.eurder.service;

import com.switchfully.eurder.eurder.api.dtos.CreateCustomerDTO;
import com.switchfully.eurder.eurder.api.dtos.CustomerDTO;
import com.switchfully.eurder.eurder.domain.Customer;
import com.switchfully.eurder.eurder.mapper.CustomerMapper;
import com.switchfully.eurder.eurder.repository.CustomerRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class CustomerService {
    private final CustomerRepository customerRepository;

    public CustomerService(CustomerRepository customerRepository) {
        this.customerRepository = customerRepository;
    }

    public CustomerDTO addCustomer(CreateCustomerDTO dto) {
        Customer customer = customerRepository.addCustomer(CustomerMapper.toObject(dto));
        return CustomerMapper.toDTO(customer);
    }

    public List<CustomerDTO> getAll() {
        List<Customer> customers = customerRepository.getAll();
        return CustomerMapper.toDTO(customers);
    }
}
