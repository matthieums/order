package com.switchfully.eurder.eurder.api;

import com.switchfully.eurder.eurder.api.dtos.CreateCustomerDTO;
import io.restassured.http.ContentType;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.server.LocalServerPort;

import static io.restassured.RestAssured.given;
import static org.hamcrest.Matchers.equalTo;
import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
class CustomerControllerTest {
    @LocalServerPort
    private int port;

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
}