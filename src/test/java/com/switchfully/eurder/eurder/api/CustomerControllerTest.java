package com.switchfully.eurder.eurder.api;

import com.switchfully.eurder.eurder.api.dtos.CreateCustomerDTO;
import com.switchfully.eurder.eurder.api.dtos.CustomerDTO;
import com.switchfully.eurder.eurder.domain.user.Customer;
import com.switchfully.eurder.eurder.factory.CreateCustomerDTOFactory;
import com.switchfully.eurder.eurder.factory.UserFactory;
import com.switchfully.eurder.eurder.repository.UserRepository;
import io.restassured.common.mapper.TypeRef;
import io.restassured.http.ContentType;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.server.LocalServerPort;

import java.util.List;

import static io.restassured.RestAssured.given;
import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.equalTo;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
class CustomerControllerTest {
    @LocalServerPort
    private int port;

    @Autowired
    UserRepository userRepository;

    @BeforeEach
    void setup() {
        userRepository.clear();
    }

    @Test
    void givenValidBody_whenPost_thenReturn201() {
        CreateCustomerDTO dto = CreateCustomerDTOFactory.validCreateCustomerDTO();

        given()
                .contentType(ContentType.JSON)
                .port(port)
                .body(dto)
                .when()
                .post("/customers")
                .then()
                .statusCode(201)
                .body("firstname", equalTo("newUser"))
                .body("lastname", equalTo("user"));

    }

    @Test
    void givenADatabaseWithCustomers_whenGetAll_thenReturnAllCustomers() {
        userRepository.addCustomer(
                UserFactory.customerWithValidDetails()
        );
        userRepository.addCustomer(
                UserFactory.customerWithValidDetails()
        );

        List<CustomerDTO> customers =
                given()
                        .port(port)
                        .when()
                        .get("/customers")
                        .then()
                        .statusCode(200)
                        .extract()
                        .as(new TypeRef<List<CustomerDTO>>() {
                        });

        assertThat(customers).hasSize(2);
    }

    @Test
    void givenADatabaseWithCustomers_whenGetCustomer_thenReturnCustomerDTO() {
        Customer customer = userRepository.addCustomer(
                UserFactory.customerWithValidDetails()
        );

        given()
                .port(port)
                .when()
                .get("/customers/" + customer.getEmail())
                .then()
                .statusCode(200)
                .body("email", equalTo(customer.getEmail())
                );
    }


}