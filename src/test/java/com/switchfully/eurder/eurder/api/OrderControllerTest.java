package com.switchfully.eurder.eurder.api;

import com.switchfully.eurder.eurder.api.dtos.CreateItemDTO;
import com.switchfully.eurder.eurder.api.dtos.CreateOrderDTO;
import com.switchfully.eurder.eurder.api.dtos.OrderDTO;
import com.switchfully.eurder.eurder.api.dtos.ReportDTO;
import com.switchfully.eurder.eurder.domain.Eurder;
import com.switchfully.eurder.eurder.domain.user.Customer;
import com.switchfully.eurder.eurder.domain.Item;
import com.switchfully.eurder.eurder.factory.DTOFactory;
import com.switchfully.eurder.eurder.factory.ItemFactory;
import com.switchfully.eurder.eurder.factory.OrderFactory;
import com.switchfully.eurder.eurder.factory.UserFactory;
import com.switchfully.eurder.eurder.mapper.OrderMapper;
import com.switchfully.eurder.eurder.repository.ItemRepository;
import com.switchfully.eurder.eurder.repository.OrderRepository;
import com.switchfully.eurder.eurder.repository.UserRepository;
import com.switchfully.eurder.eurder.service.OrderService;
import io.restassured.http.ContentType;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.server.LocalServerPort;

import java.util.List;
import java.util.Map;

import static io.restassured.RestAssured.given;
import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.equalTo;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class OrderControllerTest {
    @LocalServerPort
    private int port;

    @Autowired
    private ItemRepository itemRepository;

    @Autowired
    private UserRepository userRepository;

    private OrderService orderService;

    private Customer customer;

    @Autowired
    private OrderRepository orderRepository;

    @BeforeEach
    void setup() {
        itemRepository.clear();
        userRepository.clear();
        customer = UserFactory.customerWithValidDetails();
        userRepository.addCustomer(customer);
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
                .auth()
                .preemptive()
                .basic(customer.getId(), customer.getPassword())
                .get("/orders/" + customerId)
                .then()
                .statusCode(200)
                .extract().as(ReportDTO.class);
    }

    @Test
    void givenValidBody_whenPost_thenReturn201() {
        Item item1 = new Item("item1", "an item", 5.0, 20);
        Item item2 = new Item("item2", "an item", 5.0, 20);
        itemRepository.addItems(List.of(item1, item2));

        CreateOrderDTO dto = new CreateOrderDTO(
                Map.of(
                        item1.getId(), 1,
                        item2.getId(), 1
                ),
                customer.getId()
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
                customer.getId()
        );
        CreateOrderDTO dto2 = new CreateOrderDTO(
                Map.of(
                        item3.getId(), item3.getAmount(),
                        item4.getId(), item4.getAmount()
                ),
                customer.getId()
        );

        postOrder(dto1);
        postOrder(dto2);

        ReportDTO report = viewOrders(customer.getId());

        assertThat(report).isNotNull();
        assertThat(report.getOrderDTOS()).hasSize(2);
    }

    @Test
    void givenACustomerAndTheIdOfAnOrderHePlaced_whenReorder_ThenReturn201() {
        Item item = ItemFactory.itemWithInfiniteSupply();
        itemRepository.addItem(item);
        Eurder eurder = OrderFactory.order(item, 1, customer.getId());
        orderRepository.addOrder(eurder);

        given()
                .contentType(ContentType.JSON)
                .port(port)
                .auth()
                .preemptive()
                .basic(customer.getId(), customer.getPassword())
                .when()
                .post("/orders/" + eurder.getId() + "/reorder")
                .then()
                .statusCode(201);
    }

    @Test
    void givenACustomerAndTheIdOfAnOrderThatDoesNotExist_whenReorder_ThenReturn404() {
        Item item = ItemFactory.itemWithInfiniteSupply();
        itemRepository.addItem(item);
        Eurder eurder = OrderFactory.order(item, 1, customer.getId());

        given()
                .contentType(ContentType.JSON)
                .port(port)
                .auth()
                .preemptive()
                .basic(customer.getId(), customer.getPassword())
                .when()
                .post("/orders/" + eurder.getId() + "/reorder")
                .then()
                .statusCode(404);
    }

    @Test
    void givenACustomerAndTheIdOfAnOrderOfAnotherCustomer_whenReorder_ThenReturn404() {
        Item item = ItemFactory.itemWithInfiniteSupply();
        itemRepository.addItem(item);
        Eurder order = OrderFactory.order(item, 1, customer.getId());
        orderRepository.addOrder(order);
        Customer other = UserFactory.customerWithValidDetails();
        userRepository.addCustomer(other);

        given()
                .contentType(ContentType.JSON)
                .port(port)
                .auth()
                .preemptive()
                .basic(other.getId(), other.getPassword())
                .when()
                .post("/orders/" + order.getId() + "/reorder")
                .then()
                .statusCode(404);
    }
}
