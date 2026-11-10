package com.switchfully.eurder.eurder.api;

import com.switchfully.eurder.eurder.api.dtos.CreateCustomerDTO;
import com.switchfully.eurder.eurder.api.dtos.CustomerDTO;
import com.switchfully.eurder.eurder.domain.user.Customer;
import com.switchfully.eurder.eurder.domain.user.Feature;
import com.switchfully.eurder.eurder.factory.UserFactory;
import com.switchfully.eurder.eurder.service.CustomerService;
import com.switchfully.eurder.eurder.service.OrderService;
import com.switchfully.eurder.eurder.service.SecurityService;
import io.restassured.http.ContentType;
import io.restassured.module.mockmvc.RestAssuredMockMvc;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.webmvc.test.autoconfigure.AutoConfigureMockMvc;
import org.springframework.boot.webmvc.test.autoconfigure.WebMvcTest;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;

import java.util.List;

import static io.restassured.module.mockmvc.RestAssuredMockMvc.given;
import static org.hamcrest.Matchers.equalTo;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@WebMvcTest(CustomerController.class)
@AutoConfigureMockMvc
class CustomerControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockitoBean
    private CustomerService customerService;

    @MockitoBean
    private SecurityService securityService;

    private String authorization;


    @BeforeEach
    void setUp() {
        RestAssuredMockMvc.mockMvc(mockMvc);
        authorization = "Bearer test-token";
    }

    @Test
    void viewCustomers_shouldReturn200_whenCustomersAreRequested() {
        Customer customer = UserFactory.customerWithValidDetails();
        CustomerDTO customerDTO = new CustomerDTO(
                customer.getFirstname(),
                customer.getLastname(),
                customer.getEmail(),
                customer.getAddress(),
                customer.getPhoneNumber()
        );

        List<CustomerDTO> response = List.of(customerDTO);

        when(customerService.getAll()).thenReturn(response);

        given()
                .header("Authorization", authorization)
                .when()
                .get("/customers")
                .then()
                .statusCode(200)
                .contentType(ContentType.JSON)
                .body("size()", equalTo(1))
                .body("[0].firstname", equalTo(customer.getFirstname()))
                .body("[0].lastname", equalTo(customer.getLastname()))
                .body("[0].email", equalTo(customer.getEmail()));

        verify(securityService).validateAuthorization(
                authorization, Feature.VIEW_ALL_CUSTOMERS
        );

        verify(customerService).getAll();
    }

    @Test
    void viewCustomer_shouldReturn200_whenCustomerIsFound() {
        Long customerId = 1L;
        Customer customer = UserFactory.customerWithValidDetails();
        CustomerDTO response = new CustomerDTO(
                customer.getFirstname(),
                customer.getLastname(),
                customer.getEmail(),
                customer.getAddress(),
                customer.getPhoneNumber()
        );

        when(customerService.get(customerId)).thenReturn(response);

        given()
                .header("Authorization", authorization)
                .when()
                .get("/customers/{id}", customerId)
                .then()
                .statusCode(200)
                .contentType(ContentType.JSON)
                .body("email", equalTo(customer.getEmail()));

        verify(securityService).validateAuthorization(
                authorization, Feature.VIEW_CUSTOMER_DETAILS
        );

        verify(customerService).get(customerId);
    }

    @Test
    void addCustomer_shouldReturn201_whenCustomerIsCreated() {
        Customer customer = UserFactory.customerWithValidDetails();
        CustomerDTO response = new CustomerDTO(
                customer.getFirstname(),
                customer.getLastname(),
                customer.getEmail(),
                customer.getAddress(),
                customer.getPhoneNumber()
        );
        CreateCustomerDTO request =new CreateCustomerDTO(
                customer.getFirstname(),
                customer.getLastname(),
                customer.getEmail(),
                customer.getAddress(),
                customer.getPhoneNumber(),
                customer.getPassword()
        );

        when(customerService.addCustomer(any(CreateCustomerDTO.class)))
                .thenReturn(response);

        given()
                .contentType(ContentType.JSON)
                .body(request)
                .when()
                .post("/customers")
                .then()
                .statusCode(201)
                .contentType(ContentType.JSON)
                .body("email", equalTo(customer.getEmail()));

        verify(customerService).addCustomer(request);
    }
}