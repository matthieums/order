package com.switchfully.eurder.eurder.repository;

import com.switchfully.eurder.eurder.domain.Customer;
import com.switchfully.eurder.eurder.exception.CustomerAlreadyExistsException;
import com.switchfully.eurder.eurder.exception.CustomerDoesNotExistException;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.concurrent.ConcurrentHashMap;

@Component
public class CustomerRepository implements Clearable {
    private final Map<String, Customer> customers;

    public CustomerRepository() {
        this.customers = new ConcurrentHashMap<>();
    }

    @Override
    public void clear() {
        customers.clear();
    }

    public Customer addCustomer(Customer customer) {
        Customer existing = customers.putIfAbsent(customer.email(), customer);

        if (existing != null) {
            throw new CustomerAlreadyExistsException(customer.email());
        }

        return customer;
    }

    public List<Customer> getAll() {
        return new ArrayList<>(customers.values());
    }

    public Customer get(String id) {
        return Optional.ofNullable(customers.get(id))
                .orElseThrow(() -> new CustomerDoesNotExistException(id));
    }

    public boolean contains(Customer customer) {
        return customers.containsValue(customer);
    }
}
