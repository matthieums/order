package com.switchfully.eurder.eurder.api;

import com.switchfully.eurder.eurder.api.dtos.CreateItemDTO;
import com.switchfully.eurder.eurder.api.dtos.ItemDTO;
import com.switchfully.eurder.eurder.domain.user.Admin;
import com.switchfully.eurder.eurder.domain.user.User;
import com.switchfully.eurder.eurder.domain.user.UserType;
import com.switchfully.eurder.eurder.factory.UserFactory;
import com.switchfully.eurder.eurder.repository.ItemRepository;
import com.switchfully.eurder.eurder.repository.UserRepository;
import com.switchfully.eurder.eurder.service.ItemService;
import io.restassured.http.ContentType;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.server.LocalServerPort;

import static io.restassured.RestAssured.given;
import static org.hamcrest.Matchers.equalTo;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
class ItemControllerTest {
    @LocalServerPort
    private int port;

    @Autowired
    private ItemService itemService;
    @Autowired
    private ItemRepository itemRepository;
    @Autowired
    private UserRepository userRepository;

    private Admin admin;

    @BeforeEach
    void setup() {
        itemRepository.clear();
        userRepository.clear();
        itemRepository = new ItemRepository();
        itemService = new ItemService(itemRepository);

        admin = UserFactory.admin();
        userRepository.addAdmin(admin);
    }

    @Test
    void givenValidBody_whenAdminAddsItem_thenReturn201() {
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
                .auth()
                .preemptive()
                .basic(admin.getEmail(), admin.getPassword())
                .when()
                .post("/items")
                .then()
                .statusCode(201)
                .body("name", equalTo("object"))
                .body("description", equalTo("an object"))
                .body("price", equalTo(1.5f))
                .body("amount", equalTo(2));
    }

    @Test
    void givenNonAdminUser_whenAddItem_thenReturn401() {
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
                .statusCode(401);
    }

    @Test
    void givenValidBody_whenNonAdminPuts_thenReturn401() {
        ItemDTO originalItem = itemService.addItem(
                new CreateItemDTO(
                        "normal object",
                        "a normal object",
                        4.0d,
                        5
                )
        );

        CreateItemDTO updateDTO = new CreateItemDTO(
                "updated object",
                "an updated object",
                5.0d,
                10
        );

        given()
                .contentType(ContentType.JSON)
                .port(port)
                .body(updateDTO)
                .when()
                .put("/items/{id}", originalItem.id())
                .then()
                .statusCode(401);
    }


}
