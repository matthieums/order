package com.switchfully.eurder.eurder.api;

import com.switchfully.eurder.eurder.api.dtos.CreateItemDTO;
import com.switchfully.eurder.eurder.api.dtos.ItemDTO;
import com.switchfully.eurder.eurder.domain.Item;
import com.switchfully.eurder.eurder.domain.ItemFactory;
import com.switchfully.eurder.eurder.domain.user.Feature;
import com.switchfully.eurder.eurder.service.ItemService;
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

import static io.restassured.module.mockmvc.RestAssuredMockMvc.given;
import static org.hamcrest.Matchers.equalTo;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@WebMvcTest(ItemController.class)
@AutoConfigureMockMvc
class ItemControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockitoBean
    private ItemService itemService;

    @MockitoBean
    private SecurityService securityService;

    private String authorization;

    @BeforeEach
    void setUp() {
        RestAssuredMockMvc.mockMvc(mockMvc);
        authorization = "Bearer test-token";
    }

    @Test
    void addItem_shouldReturn201_whenItemIsCreated() {
        Item item = ItemFactory.itemWithAmount(10);
        CreateItemDTO request = new CreateItemDTO(item.getName(), item.getDescription(), item.getPrice(), item.getAmount());
        ItemDTO response = new ItemDTO(null, item.getName(), item.getDescription(), item.getPrice());

        when(itemService.addItem(any(CreateItemDTO.class)))
                .thenReturn(response);

        given()
                .header("Authorization", authorization)
                .contentType(ContentType.JSON)
                .body(request)
                .when()
                .post("/items")
                .then()
                .statusCode(201)
                .contentType(ContentType.JSON)
                .body("name", equalTo(item.getName()))
                .body("description", equalTo(item.getDescription()))
                .body("price", equalTo(item.getPrice().floatValue()));

        verify(securityService).validateAuthorization(
                authorization, Feature.ADD_ITEM
        );

        verify(itemService).addItem(request);
    }

    @Test
    void updateItem_shouldReturn202_whenItemIsUpdated() {
        Long itemId = 1L;
        Item item = ItemFactory.itemWithAmount(10);
        CreateItemDTO request = new CreateItemDTO(item.getName(), item.getDescription(), item.getPrice(), item.getAmount());
        ItemDTO response = new ItemDTO(itemId, item.getName(), item.getDescription(), item.getPrice());

        when(itemService.updateItem(eq(itemId), any(CreateItemDTO.class)))
                .thenReturn(response);

        given()
                .header("Authorization", authorization)
                .contentType(ContentType.JSON)
                .body(request)
                .when()
                .put("/items/{id}", itemId)
                .then()
                .statusCode(202)
                .contentType(ContentType.JSON)
                .body("id", equalTo(itemId.intValue()))
                .body("name", equalTo(item.getName()))
                .body("description", equalTo(item.getDescription()))
                .body("price", equalTo(item.getPrice().floatValue()));

        verify(securityService).validateAuthorization(
                authorization, Feature.UPDATE_ITEM
        );

        verify(itemService).updateItem(itemId, request);
    }
}