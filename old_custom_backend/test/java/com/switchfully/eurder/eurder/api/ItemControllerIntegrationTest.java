package com.switchfully.eurder.eurder.api;

import com.switchfully.eurder.eurder.api.dtos.CreateCustomerDTO;
import com.switchfully.eurder.eurder.api.dtos.CreateItemDTO;
import com.switchfully.eurder.eurder.api.dtos.CustomerDTO;
import com.switchfully.eurder.eurder.domain.Item;
import com.switchfully.eurder.eurder.domain.ItemFactory;
import com.switchfully.eurder.eurder.domain.user.Admin;
import com.switchfully.eurder.eurder.domain.user.Customer;
import com.switchfully.eurder.eurder.factory.UserFactory;
import com.switchfully.eurder.eurder.repository.ItemRepository;
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

import java.math.BigDecimal;

import static io.restassured.RestAssured.given;
import static org.hamcrest.Matchers.*;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@ActiveProfiles("test")
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
public class ItemControllerIntegrationTest {

    @LocalServerPort
    private int port;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private ItemRepository itemRepository;

    private CreateItemDTO randomItemRequest;
    private Item item;
    private Admin admin;

    @BeforeEach
    void setUp() {
        RestAssured.port = port;
        itemRepository.deleteAll();
        userRepository.deleteAll();

        item = itemRepository.saveAndFlush(
                ItemFactory.itemWithInfiniteSupply()
        );

        randomItemRequest = new CreateItemDTO(
                item.getName(),
                item.getDescription(),
                item.getPrice(),
                item.getAmount()
        );
        admin = userRepository.saveAndFlush(UserFactory.admin());
    }

    @Test
    void shouldCreateItemIfAdmin() {
        CreateItemDTO request = new CreateItemDTO(
                "Laptop",
                "Gaming laptop",
                new BigDecimal("10.00"),
                100
        );
        given()
                .auth().preemptive().basic(admin.getEmail(), admin.getPassword())
                .contentType("application/json")
                .body(request)
                .when()
                .post("/items")
                .then()
                .statusCode(HttpStatus.CREATED.value())
                .contentType("application/json")
                .body("id", notNullValue())
                .body("name", equalTo("Laptop"))
                .body("description", equalTo("Gaming laptop"))
                .body("price", equalTo((float)10));
    }

    @Test
    void shouldUpdateItemIfAdmin() {
        CreateItemDTO updateRequest = new CreateItemDTO(
                "Updated Laptop",
                "Updated gaming laptop",
                new BigDecimal("15.00"),
                10
        );

        given()
                .auth().preemptive().basic(admin.getEmail(), admin.getPassword())
                .contentType("application/json")
                .body(updateRequest)
                .when()
                .put("/items/" + item.getId())
                .then()
                .statusCode(HttpStatus.ACCEPTED.value())
                .contentType("application/json")
                .body("id", equalTo(item.getId().intValue()))
                .body("name", equalTo("Updated Laptop"))
                .body("description", equalTo("Updated gaming laptop"))
                .body("price", equalTo((float)15));
    }

    @Test
    void shouldRejectRequestToCreateItem_whenNoAuthorization() {
        given()
                .contentType("application/json")
                .body(randomItemRequest)
                .when()
                .post("/items")
                .then()
                .statusCode(HttpStatus.UNAUTHORIZED.value());
    }

    @Test
    void shouldRejectRequestToUpdateItem_whenNoAuthorization() {
        given()
                .contentType("application/json")
                .body(randomItemRequest)
                .when()
                .put("/items/1")
                .then()
                .statusCode(HttpStatus.UNAUTHORIZED.value());
    }

    @Test
    void getOverview_shouldRejectRequest_whenNoAuthorization() {
        given()
                .contentType("application/json")
                .when()
                .get("items/overview")
                .then()
                .statusCode(HttpStatus.UNAUTHORIZED.value());
    }

    @Test
    void getOverview_shouldReturnOverview_whenAuthorization() {
        given()
                .auth().preemptive().basic(admin.getEmail(), admin.getPassword())
                .contentType("application/json")
                .when()
                .get("items/overview")
                .then()
                .statusCode(HttpStatus.OK.value());
    }
}
