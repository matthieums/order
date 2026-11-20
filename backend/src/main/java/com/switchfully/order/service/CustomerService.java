package com.switchfully.order.service;

import com.switchfully.order.domain.Customer;
import com.switchfully.order.repository.CustomerRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.UUID;

@Service
public class CustomerService {

    private final CustomerRepository customerRepository;
    private final CustomerValidator customerValidator;

    public CustomerService(CustomerRepository customerRepository, CustomerValidator customerValidator) {
        this.customerRepository = customerRepository;
        this.customerValidator = customerValidator;
    }

    @Transactional(propagation = Propagation.REQUIRED)
    public Customer createCustomer(Customer customer) {
        if (!customerValidator.isValidForCreation(customer)) {
            customerValidator.throwInvalidStateException(customer, "creation");
        }
        return customerRepository.save(customer);
    }

    @Transactional(propagation = Propagation.SUPPORTS)
    public List<Customer> getAllCustomers() {
        return customerRepository.getAll();
    }

    @Transactional(propagation = Propagation.SUPPORTS)
    public Customer getCustomer(UUID id) {
        return customerRepository.get(id);
    }

    @Transactional(propagation = Propagation.REQUIRED)
    public Customer updateCustomer(Customer customer) {
        if (!customerValidator.isValidForUpdating(customer)) {
            customerValidator.throwInvalidStateException(customer, "updating");
        }
        return customerRepository.update(customer);
    }
}
