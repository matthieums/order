package com.switchfully.eurder.eurder.api;

import com.switchfully.eurder.eurder.api.dtos.CreateItemDTO;
import com.switchfully.eurder.eurder.api.dtos.CreateOrderDTO;
import com.switchfully.eurder.eurder.api.dtos.OrderDTO;
import com.switchfully.eurder.eurder.api.dtos.ReportDTO;
import com.switchfully.eurder.eurder.domain.Customer;
import com.switchfully.eurder.eurder.domain.Item;
import com.switchfully.eurder.eurder.domain.ItemGroup;
import com.switchfully.eurder.eurder.repository.ItemRepository;
import com.switchfully.eurder.eurder.service.OrderService;
import io.restassured.http.ContentType;
import org.assertj.core.api.Assertions.*;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.server.LocalServerPort;

import java.util.List;
import java.util.Map;

import static io.restassured.RestAssured.given;
import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class OrderControllerTest {
    @LocalServerPort
    private int port;
    @Autowired
    private ItemRepository itemRepository;
    @Autowired
    private OrderService orderService;
    private Customer customer;

    @BeforeEach
    void setup() {
        customer = new Customer(
                "newUser",
                "user",
                "user@dto.com",
                "userAddress - 5555",
                "04 555 555 555"
        );
    }

    private OrderDTO postOrder(CreateOrderDTO dto) {
        return given()
                .contentType(ContentType.JSON)
                .port(port)
                .body(dto)
                .when()
                .post("/orders")
                .then()
                .statusCode(201)
                .extract().as(OrderDTO.class);
    }

    private ReportDTO viewOrders(String customerId) {
        return given()
                .port(port)
                .when()
                .get("/orders/" + customerId)
                .then()
                .statusCode(200)
                .extract().as(ReportDTO.class);
    }

    @Test
    void givenValidBody_whenPost_thenReturn201() {
        Item item1 = new Item("item1", "an item", 5.0, 20);
        Item item2 = new Item("item2", "an item", 5.0, 20);
        CreateOrderDTO dto = new CreateOrderDTO(
                Map.of(
                        item1.getId(), item1.getAmount(),
                        item2.getId(), item2.getAmount()
                ),
                customer.email()
        );

        OrderDTO result = postOrder(dto);

        assertThat(result).isNotNull();
        assertThat(result.items()).hasSize(2);
    }

    @Test
    void givenARepositoryContainingOrders_whenViewReportWithCustomerId_DisplayCustomerOrdersAnd200() {
        Item item1 = new Item("item1", "item", 2.0, 20);
        Item item2 = new Item("item2", "item", 2.0, 20);
        Item item3 = new Item("item3", "item", 2.0, 20);
        Item item4 = new Item("item4", "item", 2.0, 20);
        itemRepository.addItems(
                List.of(item1, item2, item3, item4)
        );

        CreateOrderDTO dto1 = new CreateOrderDTO(
                Map.of(
                        item1.getId(), item1.getAmount(),
                        item2.getId(), item2.getAmount()
                ),
                customer.email()
        );
        CreateOrderDTO dto2 = new CreateOrderDTO(
                Map.of(
                        item3.getId(), item3.getAmount(),
                        item4.getId(), item4.getAmount()
                ),
                customer.email()
        );

        postOrder(dto1);
        postOrder(dto2);

        ReportDTO report = viewOrders(customer.email());

        assertThat(report).isNotNull();
        assertThat(report.getOrderDTOS()).hasSize(2);
    }
}
