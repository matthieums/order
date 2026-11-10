package com.switchfully.eurder.eurder.api;

import com.switchfully.eurder.eurder.api.dtos.CreateCustomerDTO;
import com.switchfully.eurder.eurder.api.dtos.CustomerDTO;
import com.switchfully.eurder.eurder.domain.user.Feature;
import com.switchfully.eurder.eurder.service.CustomerService;
import com.switchfully.eurder.eurder.service.SecurityService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping(value = "customers")
public class CustomerController {
    private static final String APPLICATION_JSON = "application/json";

    private final CustomerService customerService;
    private final SecurityService securityService;

    public CustomerController(CustomerService customerService, SecurityService securityService) {
        this.customerService = customerService;
        this.securityService = securityService;
    }

    @GetMapping(produces = APPLICATION_JSON)
    @ResponseStatus(HttpStatus.OK)
    @Operation(summary = "View all customers", description = "Retrieves all customers")
    @ApiResponse(responseCode = "200", description = "Customer found")
    @ApiResponse(responseCode = "404", description = "Customer not found")
    public List<CustomerDTO> viewCustomers(@RequestHeader(value = "Authorization", required = false) String authorization) {
        securityService.validateAuthorization(authorization, Feature.VIEW_ALL_CUSTOMERS);
        return customerService.getAll();
    }

    @GetMapping(value = "/{id}", produces = APPLICATION_JSON)
    @ResponseStatus(HttpStatus.OK)
    @Operation(summary = "View customer info", description = "Retrieves customer information by its ID")
    @ApiResponse(responseCode = "200", description = "Customer found")
    @ApiResponse(responseCode = "404", description = "Customer not found")
    public CustomerDTO viewCustomer(@RequestHeader(value = "Authorization", required = false) String authorization, @PathVariable Long id) {
        securityService.validateAuthorization(authorization, Feature.VIEW_CUSTOMER_DETAILS);
        return customerService.get(id);
    }

    @PostMapping(consumes = APPLICATION_JSON, produces = APPLICATION_JSON)
    @Operation(summary = "Add a new customer", description = "Adds a new customer")
    @ApiResponse(responseCode = "201", description = "Customer added")
    @ApiResponse(responseCode = "409", description = "Customer already exists")
    @ApiResponse(responseCode = "400", description = "Invalid input")
    @ApiResponse(responseCode = "500", description = "Customer could not be added")
    @ResponseStatus(HttpStatus.CREATED)
    public CustomerDTO addCustomer(@Valid @RequestBody CreateCustomerDTO createCustomerDTO) {
        return customerService.addCustomer(createCustomerDTO);
    }
}
