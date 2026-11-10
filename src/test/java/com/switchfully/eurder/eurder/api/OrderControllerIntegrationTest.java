package com.switchfully.eurder.eurder.api;

import com.switchfully.eurder.eurder.api.dtos.*;
import com.switchfully.eurder.eurder.domain.Eurder;
import com.switchfully.eurder.eurder.domain.Item;
import com.switchfully.eurder.eurder.domain.ItemFactory;
import com.switchfully.eurder.eurder.domain.ItemGroup;
import com.switchfully.eurder.eurder.domain.user.Admin;
import com.switchfully.eurder.eurder.domain.user.Customer;
import com.switchfully.eurder.eurder.factory.UserFactory;
import com.switchfully.eurder.eurder.repository.*;
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
import java.time.LocalDate;
import java.util.List;

import static io.restassured.RestAssured.given;
import static org.hamcrest.Matchers.*;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@ActiveProfiles("test")
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
public class OrderControllerIntegrationTest {

    @LocalServerPort
    private int port;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private ItemRepository itemRepository;

    @Autowired
    private OrderRepository orderRepository;

    @Autowired
    private ItemGroupRepository itemGroupRepository;

    private Admin admin;
    private Customer customer;
    private Item item;

    @BeforeEach
    void setUp() {
        RestAssured.port = port;

        itemGroupRepository.deleteAll();
        orderRepository.deleteAll();
        itemRepository.deleteAll();
        userRepository.deleteAll();

        admin = userRepository.saveAndFlush(UserFactory.admin());
        customer = userRepository.saveAndFlush(
                UserFactory.customerWithValidDetails()
        );

        item = itemRepository.saveAndFlush(
                new Item(
                        "Laptop",
                        "Gaming laptop",
                        new BigDecimal("1000.00"),
                        20
                )
        );
    }

    private CreateOrderDTO createOrderRequest() {
        return new CreateOrderDTO(
                List.of(new CreateItemGroupDTO(item.getId(), 2)),
                customer.getId()
        );
    }

    @Test
    void shouldCreateOrder() {
        given()
                .contentType("application/json")
                .body(createOrderRequest())
                .when()
                .post("/orders")
                .then()
                .statusCode(HttpStatus.CREATED.value())
                .contentType("application/json")
                .body("id", notNullValue())
                .body("itemGroups", hasSize(1))
                .body("itemGroups[0].itemName", equalTo("Laptop"))
                .body("itemGroups[0].orderedAmount", equalTo(2))
                .body("itemGroups[0].totalPrice", equalTo(2000.0f))
                .body("totalPrice", equalTo(2000.0f));
    }

    @Test
    void shouldRejectOrderWithNonExistingCustomer() {
        CreateOrderDTO request = new CreateOrderDTO(
                List.of(new CreateItemGroupDTO(item.getId(), 2)),
                99999L
        );

        given()
                .contentType("application/json")
                .body(request)
                .when()
                .post("/orders")
                .then()
                .statusCode(HttpStatus.NOT_FOUND.value());
    }

    @Test
    void shouldRejectOrderWithNonExistingItem() {
        CreateOrderDTO request = new CreateOrderDTO(
                List.of(new CreateItemGroupDTO(99999L, 2)),
                customer.getId()
        );

        given()
                .contentType("application/json")
                .body(request)
                .when()
                .post("/orders")
                .then()
                .statusCode(HttpStatus.NOT_FOUND.value());
    }

    @Test
    void shouldGetCustomerReportOwnOrder() {
        given()
                .contentType("application/json")
                .body(createOrderRequest())
                .post("/orders");

        given()
                .auth().preemptive().basic(
                        customer.getEmail(),
                        customer.getPassword()
                )
                .when()
                .get("/orders/" + customer.getId())
                .then()
                .statusCode(HttpStatus.OK.value())
                .contentType("application/json")
                .body("customerId", equalTo(customer.getId().intValue()))
                .body("orders", hasSize(1))
                .body("orders[0].orderId", notNullValue())
                .body("orders[0].itemGroups", hasSize(1))
                .body("orders[0].itemGroups[0].itemName", equalTo("Laptop"))
                .body("orders[0].itemGroups[0].orderedAmount", equalTo(2))
                .body("orders[0].totalPrice", equalTo(2000.0f));
    }

    @Test
    void shouldRejectReportRequestWithoutAuthorization() {
        given()
                .when()
                .get("/orders/" + customer.getId())
                .then()
                .statusCode(HttpStatus.UNAUTHORIZED.value());
    }

    @Test
    void shouldReorderOwnOrder() {
        Integer orderId = given()
                .contentType("application/json")
                .body(createOrderRequest())
                .when()
                .post("/orders")
                .then()
                .statusCode(HttpStatus.CREATED.value())
                .extract()
                .path("id");

        given()
                .auth().preemptive().basic(
                        customer.getEmail(),
                        customer.getPassword()
                )
                .when()
                .post("/orders/" + orderId + "/reorder")
                .then()
                .statusCode(HttpStatus.CREATED.value())
                .contentType("application/json")
                .body("id", notNullValue())
                .body("itemGroups", hasSize(1))
                .body("totalPrice", equalTo(2000.0f));
    }

    @Test
    void shouldRejectReorderWithoutAuthorization() {
        given()
                .when()
                .post("/orders/1/reorder")
                .then()
                .statusCode(HttpStatus.UNAUTHORIZED.value());
    }

    @Test
    void shouldGetOrdersShippingTodayIfAdmin() {
        Eurder order = orderRepository.saveAndFlush(
                new Eurder(customer)
        );

        ItemGroup group = new ItemGroup(order, item, 2);
        group.setShippingDate(LocalDate.now());

        itemGroupRepository.saveAndFlush(group);

        given()
                .auth().preemptive().basic(
                        admin.getEmail(),
                        admin.getPassword()
                )
                .when()
                .get("/orders/shipping_today")
                .then()
                .statusCode(HttpStatus.OK.value())
                .contentType("application/json")
                .body("$", hasSize(1))
                .body("[0].shippingAddress",
                        equalTo(customer.getAddress()))
                .body("[0].itemGroups", hasSize(1));
    }

    @Test
    void shouldRejectShippingRequestWithoutAuthorization() {
        given()
                .when()
                .get("/orders/shipping_today")
                .then()
                .statusCode(HttpStatus.UNAUTHORIZED.value());
    }
}
