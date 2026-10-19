package com.switchfully.eurder.eurder.api;

import com.switchfully.eurder.eurder.api.dtos.CreateItemDTO;
import io.restassured.http.ContentType;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.server.LocalServerPort;

import static io.restassured.RestAssured.given;
import static org.hamcrest.Matchers.equalTo;
import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)

class ItemControllerTest {
    @LocalServerPort
    private int port;

    @Test
    void givenValidBody_whenPost_thenReturn201() {
        CreateItemDTO dto = new CreateItemDTO(
                "object",
                "an object",
                1.5d,
                2
        );

        given()
                .contentType(ContentType.JSON)
                .port(port)
                .body(dto)
                .when()
                .post("/items")
                .then()
                .statusCode(201)
                .body("name", equalTo("object"))
                .body("description", equalTo("an object"))
                .body("price", equalTo(1.5f))
                .body("amount", equalTo(2));
    }
}