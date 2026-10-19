package com.switchfully.eurder.eurder.repository;

import com.switchfully.eurder.eurder.domain.Customer;
import org.springframework.stereotype.Component;

import java.util.Map;
import java.util.Optional;
import java.util.concurrent.ConcurrentHashMap;

@Component
public class CustomerRepository {
    private Map<String, Customer> customers;

    public CustomerRepository() {
        this.customers = new ConcurrentHashMap<>();
    }

    public Customer addCustomer(Customer customer) {
        return customers.put(customer.getEmail(), customer);
    }

    public boolean exists(Customer customer) {
        return customers.containsValue(customer);
    }
}
