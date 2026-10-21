package com.switchfully.eurder.eurder.api;

import com.switchfully.eurder.eurder.api.dtos.CreateCustomerDTO;
import com.switchfully.eurder.eurder.api.dtos.CustomerDTO;
import com.switchfully.eurder.eurder.domain.Customer;
import com.switchfully.eurder.eurder.repository.CustomerRepository;
import io.restassured.common.mapper.TypeRef;
import io.restassured.http.ContentType;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.server.LocalServerPort;

import java.util.List;

import static io.restassured.RestAssured.given;
import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.equalTo;
import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
class CustomerControllerTest {
    @LocalServerPort
    private int port;

    @Autowired
    CustomerRepository customerRepository;

    @Test
    void givenValidBody_whenPost_thenReturn201() {
        CreateCustomerDTO dto = new CreateCustomerDTO(
                "newUser",
                "user",
                "user@dto.com",
                "userAddress - 5555",
                "04 555 555 555"
        );

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
        customerRepository.addCustomer(
            new Customer(
                    "newUser",
                    "user",
                    "user1@dto.com",
                    "userAddress - 5555",
                    "04 555 555 555"
            )
        );
        customerRepository.addCustomer(
            new Customer(
                    "newUser",
                    "user",
                    "user2@dto.com",
                    "userAddress - 5555",
                    "04 555 555 555"
            )
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
}