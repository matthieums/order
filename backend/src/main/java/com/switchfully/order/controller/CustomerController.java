package com.switchfully.order.controller;

import com.switchfully.order.dto.CustomerDto;
import com.switchfully.order.dto.CustomerOverviewDto;
import com.switchfully.order.mapper.CustomerMapper;
import com.switchfully.order.mapper.CustomerOverviewMapper;
import com.switchfully.order.service.CustomerService;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@RestController
@RequestMapping(path = "/" + CustomerController.RESOURCE_NAME)
public class CustomerController {

    public static final String RESOURCE_NAME = "customers";

    private final CustomerService customerService;
    private final CustomerMapper customerMapper;
    private final CustomerOverviewMapper customerOverviewMapper;

    public CustomerController(CustomerService customerService, CustomerMapper customerMapper, CustomerOverviewMapper customerOverviewMapper) {
        this.customerService = customerService;
        this.customerMapper = customerMapper;
        this.customerOverviewMapper = customerOverviewMapper;
    }

    @PostMapping(consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    public CustomerDto createCustomer(@RequestBody CustomerDto customerDto) {
        return customerMapper.toDto(
                customerService.createCustomer(
                        customerMapper.toDomain(customerDto)));
    }

    @PutMapping(path = "/{id}", consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    public CustomerDto updateCustomer(@PathVariable String id, @RequestBody CustomerDto customerDto) {
        return customerMapper.toDto(
                customerService.updateCustomer(
                        customerMapper.toDomain(UUID.fromString(id), customerDto)));
    }

    @GetMapping(produces = MediaType.APPLICATION_JSON_VALUE)
    public List<CustomerOverviewDto> getAllCustomers() {
        return customerService.getAllCustomers().stream()
                .map(customerOverviewMapper::toDto)
                .collect(Collectors.toList());
    }

    @GetMapping(path="/{id}", produces = MediaType.APPLICATION_JSON_VALUE)
    public CustomerDto getCustomer(@PathVariable String id) {
        return customerMapper.toDto(
                customerService.getCustomer(UUID.fromString(id)));
    }

}
