package com.switchfully.eurder.eurder.api;

import com.switchfully.eurder.eurder.api.dtos.*;
import com.switchfully.eurder.eurder.domain.user.Feature;
import com.switchfully.eurder.eurder.service.OrderService;
import com.switchfully.eurder.eurder.service.ReportService;
import com.switchfully.eurder.eurder.service.SecurityService;
import io.restassured.http.ContentType;
import io.restassured.module.mockmvc.RestAssuredMockMvc;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentCaptor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.webmvc.test.autoconfigure.AutoConfigureMockMvc;
import org.springframework.boot.webmvc.test.autoconfigure.WebMvcTest;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;

import static org.hamcrest.Matchers.hasSize;
import static org.junit.jupiter.api.Assertions.*;

import java.math.BigDecimal;
import java.util.List;

import static io.restassured.module.mockmvc.RestAssuredMockMvc.given;
import static org.hamcrest.core.IsEqual.equalTo;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@WebMvcTest(OrderController.class)
@AutoConfigureMockMvc
class OrderControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockitoBean
    private OrderService orderService;

    @MockitoBean
    private SecurityService securityService;

    @MockitoBean
    private ReportService reportService;

    @BeforeEach
    void setUp() {
        RestAssuredMockMvc.mockMvc(mockMvc);
    }

    @Test
    void addOrder_shouldReturn201_whenOrderIsCreated() {

        CreateOrderDTO request = new CreateOrderDTO(
                List.of(new CreateItemGroupDTO(2L, 2)),
                1L
        );

        OrderDetailsDTO response = new OrderDetailsDTO(
                null,
                List.of(),
                new BigDecimal("2000.00")
        );

        when(orderService.addOrder(any(CreateOrderDTO.class)))
                .thenReturn(response);

        given()
                .contentType(ContentType.JSON)
                .body(request)
                .when()
                .post("/orders")
                .then()
                .statusCode(201)
                .contentType(ContentType.JSON)
                .body("totalPrice", equalTo(2000.00F));

        ArgumentCaptor<CreateOrderDTO> captor =
                ArgumentCaptor.forClass(CreateOrderDTO.class);

        verify(orderService).addOrder(captor.capture());

        assertEquals(1L, captor.getValue().customerId());
        assertEquals(2L, captor.getValue().itemGroups().getFirst().itemId());
        assertEquals(2, captor.getValue().itemGroups().getFirst().amount());
    }

    @Test
    void viewReport_shouldReturn200_whenAuthorizationIsValid() {
        Long customerId = 1L;
        String authorization = "Bearer test-token";

        ReportDTO response = new ReportDTO(
                customerId,
                List.of(
                        new OrderReportDTO(
                                10L,
                                List.of(
                                        new ItemGroupDTO(
                                                "Laptop",
                                                2,
                                                new BigDecimal("2000.00")
                                        )
                                ),
                                new BigDecimal("2000.00")
                        )
                )
        );

        when(reportService.getReportForCustomerWithId(customerId))
                .thenReturn(response);

        given()
                .header("Authorization", authorization)
                .when()
                .get("/orders/{id}", customerId)
                .then()
                .statusCode(200)
                .contentType(ContentType.JSON)
                .body("customerId", equalTo(1))
                .body("orders[0].orderId", equalTo(10))
                .body("orders[0].itemGroups[0].itemName", equalTo("Laptop"))
                .body("orders[0].itemGroups[0].orderedAmount", equalTo(2))
                .body("orders[0].totalPrice", equalTo(2000.00F));

        verify(securityService).validateAuthorization(
                authorization, Feature.VIEW_REPORT
        );

        verify(reportService).getReportForCustomerWithId(customerId);
    }

    @Test
    void reorder_shouldReturn201_whenAuthorizationIsValid() {
        Long userId = 1L;
        Long orderId = 5L;
        String authorization = "Bearer test-token";

        OrderDetailsDTO response = new OrderDetailsDTO(
                null,
                List.of(),
                new BigDecimal("2000.00")
        );

        when(securityService.validateAuthorization(
                authorization,
                Feature.REORDER
        )).thenReturn(userId);

        when(orderService.reorder(userId, orderId))
                .thenReturn(response);

        given()
                .header("Authorization", authorization)
                .contentType(ContentType.JSON)
                .when()
                .post("/orders/{orderId}/reorder", orderId)
                .then()
                .statusCode(201)
                .contentType(ContentType.JSON)
                .body("totalPrice", equalTo(2000.00F));

        verify(securityService).validateAuthorization(
                authorization,
                Feature.REORDER
        );

        verify(orderService).reorder(userId, orderId);
    }

    @Test
    void getOrdersShippingToday_shouldReturn200_whenNoOrdersExist() {
        String authorization = "Bearer test-token";

        when(reportService.getOrdersWithItemsShippingToday())
                .thenReturn(List.of());

        given()
                .header("Authorization", authorization)
                .when()
                .get("/orders/shipping_today")
                .then()
                .statusCode(200)
                .contentType(ContentType.JSON)
                .body("", hasSize(0));

        verify(securityService).validateAuthorization(
                authorization,
                Feature.GET_ORDER_OF_ITEM_WITH_SHIPPING_DATE
        );

        verify(reportService).getOrdersWithItemsShippingToday();
    }

    @Test
    void getOrdersShippingToday_shouldReturn200AndItems_whenOrderExists() {
        String authorization = "Bearer test-token";
        String shippingAddress = "Rue du pont, 78 4020 Liège";

        when(reportService.getOrdersWithItemsShippingToday())
                .thenReturn(List.of(
                        new ShippingDTO(
                                List.of(
                                        new ItemGroupDTO(
                                                "laptop",
                                                10,
                                                new BigDecimal("1000.0")
                                        )
                                ),
                                shippingAddress
                        )
                ));

        given()
                .header("Authorization", authorization)
                .when()
                .get("/orders/shipping_today")
                .then()
                .statusCode(200)
                .contentType(ContentType.JSON)
                .body("", hasSize(1))
                .body("[0].shippingAddress", equalTo(shippingAddress))
                .body("[0].itemGroups", hasSize(1))
                .body("[0].itemGroups[0].itemName", equalTo("laptop"))
                .body("[0].itemGroups[0].orderedAmount", equalTo(10))
                .body("[0].itemGroups[0].totalPrice", equalTo(1000.0F));

        verify(securityService).validateAuthorization(
                authorization,
                Feature.GET_ORDER_OF_ITEM_WITH_SHIPPING_DATE
        );

        verify(reportService).getOrdersWithItemsShippingToday();
    }
}
