package com.switchfully.eurder.eurder.api;

import com.switchfully.eurder.eurder.api.dtos.CreateCustomerDTO;
import com.switchfully.eurder.eurder.api.dtos.CustomerDTO;
import com.switchfully.eurder.eurder.domain.user.Admin;
import com.switchfully.eurder.eurder.domain.user.Customer;
import com.switchfully.eurder.eurder.factory.UserFactory;
import com.switchfully.eurder.eurder.repository.UserRepository;
import io.restassured.RestAssured;
import io.restassured.module.mockmvc.RestAssuredMockMvc;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.server.LocalServerPort;
import org.springframework.boot.jdbc.test.autoconfigure.AutoConfigureTestDatabase;
import org.springframework.http.HttpStatus;
import org.springframework.test.context.ActiveProfiles;

import static io.restassured.RestAssured.given;
import static org.hamcrest.Matchers.*;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@ActiveProfiles("test")
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
class CustomerControllerIntegrationTest {

    @LocalServerPort
    private int port;

    @Autowired
    private UserRepository userRepository;

    private String authorization;

    @BeforeEach
    void setUp() {
        RestAssured.port = port;
        authorization = "Bearer test-token";
        userRepository.deleteAll();
    }

    @Test
    void shouldCreateCustomer() {
        Customer customer = UserFactory.customerWithValidDetails();
        CreateCustomerDTO request =new CreateCustomerDTO(
                customer.getFirstname(),
                customer.getLastname(),
                customer.getEmail(),
                customer.getAddress(),
                customer.getPhoneNumber(),
                customer.getPassword()
        );

        given()
                .contentType("application/json")
                .body(request)
                .when()
                .post("/customers")
                .then()
                .statusCode(HttpStatus.CREATED.value())
                .contentType("application/json")
                .body("id", notNullValue())
                .body("firstname", equalTo(customer.getFirstname()))
                .body("lastname", equalTo(customer.getLastname()))
                .body("email", equalTo(customer.getEmail()));
    }

    @Test
    void shouldGetAllCustomersIfAdmin() {
        Admin admin = userRepository.saveAndFlush(UserFactory.admin());
        userRepository.saveAndFlush(
                new Customer(
                        "John", "Doe", "john@test.com",
                        "password", "Some address", "0412345678"
                )
        );

        given()
                .auth().preemptive().basic(admin.getEmail(), admin.getPassword())
                .when()
                .get("/customers")
                .then()
                .statusCode(HttpStatus.OK.value())
                .contentType("application/json")
                .body("$", hasSize(1))
                .body("[0].firstname", equalTo("John"))
                .body("[0].email", equalTo("john@test.com"));
    }

    @Test
    void shouldGetCustomerByIdIfAdmin() {
        Admin admin = userRepository.saveAndFlush(UserFactory.admin());
        Customer customer = userRepository.saveAndFlush(
                new Customer(
                        "John", "Doe", "john@test.com",
                        "password", "Some address", "0412345678"
                )
        );

        given()
                .auth().preemptive().basic(admin.getEmail(), admin.getPassword())
                .when()
                .get("/customers/" + customer.getId())
                .then()
                .statusCode(HttpStatus.OK.value())
                .body("id", equalTo(customer.getId().intValue()))
                .body("firstname", equalTo("John"))
                .body("lastname", equalTo("Doe"));
    }

    @Test
    void shouldRejectRequestToViewAllCustomers_whenNoAuthorization() {
        given()
                .when()
                .get("/customers")
                .then()
                .statusCode(HttpStatus.UNAUTHORIZED.value());
    }

    @Test
    void shouldRejectRequestToCustomerDetails_whenNoAuthorization() {
        Customer customer = userRepository.saveAndFlush(
                new Customer(
                        "John", "Doe", "john@test.com",
                        "password", "Some address", "0412345678"
                )
        );
        given()
                .when()
                .get("/customers/" + customer.getId())
                .then()
                .statusCode(HttpStatus.UNAUTHORIZED.value());
    }

}