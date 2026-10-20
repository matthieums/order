package com.switchfully.eurder.eurder.api;

import com.switchfully.eurder.eurder.api.dtos.CreateOrderDTO;
import com.switchfully.eurder.eurder.api.dtos.OrderDTO;
import com.switchfully.eurder.eurder.domain.Customer;
import com.switchfully.eurder.eurder.domain.ItemGroup;
import io.restassured.http.ContentType;
import org.assertj.core.api.Assertions;
import org.assertj.core.api.Assertions.*;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.server.LocalServerPort;

import java.util.List;

import static io.restassured.RestAssured.given;
import static org.hamcrest.Matchers.equalTo;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class OrderControllerTest {
    @LocalServerPort
    private int port;

    @Test
    void givenValidBody_whenPost_thenReturn201() {
        Customer customer = new Customer(
                "newUser",
                "user",
                "user@dto.com",
                "userAddress - 5555",
                "04 555 555 555"
        );

        CreateOrderDTO dto = new CreateOrderDTO(
                List.of(
                        new ItemGroup("1491", 14),
                        new ItemGroup("54454", 1)
                ),
                customer.email()
        );

        OrderDTO result = given()
                .contentType(ContentType.JSON)
                .port(port)
                .body(dto)
                .when()
                .post("/orders")
                .then()
                .statusCode(201)
                .extract().as(OrderDTO.class);

        Assertions.assertThat(result).isNotNull();
        Assertions.assertThat(result.items()).hasSize(2);

    }
}
