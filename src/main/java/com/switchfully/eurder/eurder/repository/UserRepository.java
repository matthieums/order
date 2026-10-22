package com.switchfully.eurder.eurder.repository;

import com.switchfully.eurder.eurder.domain.user.Admin;
import com.switchfully.eurder.eurder.domain.user.Customer;
import com.switchfully.eurder.eurder.domain.user.User;
import com.switchfully.eurder.eurder.exception.CustomerAlreadyExistsException;
import com.switchfully.eurder.eurder.exception.CustomerDoesNotExistException;
import org.springframework.stereotype.Repository;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.concurrent.ConcurrentHashMap;

@Repository
public class UserRepository extends AppRepository {
    private final Map<String, User> users;

    public UserRepository() {
        this.users = new ConcurrentHashMap<>();
    }

    @Override
    public void clear() {
        users.clear();
    }

    public Admin addAdmin(Admin admin) {
        users.put(admin.getEmail(), admin);
        return admin;
    }

    public Customer addCustomer(Customer customer) {
        Customer existing = (Customer) users.putIfAbsent(customer.getId(), customer);

        if (existing != null) {
            throw new CustomerAlreadyExistsException(customer.getId());
        }

        return customer;
    }

    public List<User> getAll() {
        return new ArrayList<>(users.values());
    }

    public User get(String id) {
        return Optional.ofNullable(users.get(id))
                .orElseThrow(() -> new CustomerDoesNotExistException(id));
    }

    public boolean contains(Customer customer) {
        return users.containsValue(customer);
    }
}
