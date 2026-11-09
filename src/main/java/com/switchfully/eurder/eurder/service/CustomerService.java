package com.switchfully.eurder.eurder.service;

import com.switchfully.eurder.eurder.api.dtos.CreateCustomerDTO;
import com.switchfully.eurder.eurder.api.dtos.CustomerDTO;
import com.switchfully.eurder.eurder.domain.user.Customer;
import com.switchfully.eurder.eurder.domain.user.User;
import com.switchfully.eurder.eurder.exception.CustomerDoesNotExistException;
import com.switchfully.eurder.eurder.mapper.CustomerMapper;
import com.switchfully.eurder.eurder.repository.CustomerRepository;
import com.switchfully.eurder.eurder.repository.UserRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@Transactional
public class CustomerService {
    private final CustomerRepository customerRepository;
    private final CustomerMapper customerMapper;

    public CustomerService(CustomerRepository customerRepository, CustomerMapper customerMapper) {
        this.customerRepository = customerRepository;
        this.customerMapper = customerMapper;
    }

    public CustomerDTO addCustomer(CreateCustomerDTO dto) {
        Customer customer = customerRepository.save(customerMapper.toObject(dto));
        return customerMapper.toDTO(customer);
    }

    public List<CustomerDTO> getAll() {
        List<Customer> customers = customerRepository.findAll();;
        return customerMapper.toDTO(customers);
    }

    public CustomerDTO get(Long id) {
        Customer customer = customerRepository.findById(id)
                .orElseThrow(() -> new CustomerDoesNotExistException(id));

        return customerMapper.toDTO(customer);
    }
}
