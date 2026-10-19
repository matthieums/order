package com.switchfully.eurder.eurder.repository;

import com.switchfully.eurder.eurder.domain.Customer;
import com.switchfully.eurder.eurder.exception.CustomerAlreadyExistsException;
import org.springframework.stereotype.Component;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

@Component
public class CustomerRepository {
    private final Map<String, Customer> customers;

    public CustomerRepository() {
        this.customers = new ConcurrentHashMap<>();
    }

    public Customer addCustomer(Customer customer) {
        Customer existing = customers.putIfAbsent(customer.email(), customer);

        if (existing != null) {
            throw new CustomerAlreadyExistsException(customer.email());
        }

        return customer;
    }

    public boolean contains(Customer customer) {
        return customers.containsValue(customer);
    }
}
