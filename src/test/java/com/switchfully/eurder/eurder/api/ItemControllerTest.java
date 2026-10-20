package com.switchfully.eurder.eurder.api;

import com.switchfully.eurder.eurder.api.dtos.CreateItemDTO;
import com.switchfully.eurder.eurder.api.dtos.ItemDTO;
import com.switchfully.eurder.eurder.domain.Item;
import com.switchfully.eurder.eurder.mapper.ItemMapper;
import com.switchfully.eurder.eurder.repository.ItemRepository;
import com.switchfully.eurder.eurder.service.ItemService;
import io.restassured.http.ContentType;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.server.LocalServerPort;
import org.springframework.context.annotation.Bean;

import static io.restassured.RestAssured.given;
import static org.hamcrest.Matchers.equalTo;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
class ItemControllerTest {
    @LocalServerPort
    private int port;

    private ItemService itemService;
    private ItemRepository itemRepository;

    @BeforeEach
    void setup() {
        itemRepository = new ItemRepository();
        itemService = new ItemService(itemRepository);
    }

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

    @Test
    void givenValidBody_whenPut_thenRETURN201() {
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
                .statusCode(202)
                .body("name", equalTo("updated object"))
                .body("description", equalTo("an updated object"))
                .body("price", equalTo(5.0f))
                .body("amount", equalTo(10));
    }


}
