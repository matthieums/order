package com.switchfully.eurder.eurder.service;

import com.switchfully.eurder.eurder.api.dtos.CreateCustomerDTO;
import com.switchfully.eurder.eurder.api.dtos.CustomerDTO;
import com.switchfully.eurder.eurder.domain.user.Customer;
import com.switchfully.eurder.eurder.domain.user.User;
import com.switchfully.eurder.eurder.mapper.CustomerMapper;
import com.switchfully.eurder.eurder.repository.UserRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class CustomerService {
    private final UserRepository userRepository;

    public CustomerService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    public CustomerDTO addCustomer(CreateCustomerDTO dto) {
        Customer customer = userRepository.addCustomer(CustomerMapper.toObject(dto));
        return CustomerMapper.toDTO(customer);
    }

    public List<CustomerDTO> getAll() {
        List<User> users = userRepository.getAll();

        List<Customer> customers =  users.stream()
                .filter(User::isCustomer)
                .map(user -> (Customer) user)
                .toList();

        return CustomerMapper.toDTO(customers);
    }

    public CustomerDTO get(String customerId) {
        User user = userRepository.get(customerId);

        if (!user.isCustomer()) {
            throw new RuntimeException("Cannot fetch the required ID from the database");
        }

        return CustomerMapper.toDTO((Customer) user);

    }
}
