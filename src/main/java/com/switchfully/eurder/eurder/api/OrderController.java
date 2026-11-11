package com.switchfully.eurder.eurder.api;

import com.switchfully.eurder.eurder.api.dtos.*;
import com.switchfully.eurder.eurder.domain.user.Feature;
import com.switchfully.eurder.eurder.service.OrderService;
import com.switchfully.eurder.eurder.service.ReportService;
import com.switchfully.eurder.eurder.service.SecurityService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping(value = "orders")
public class OrderController {
    private static final String APPLICATION_JSON = "application/json";
    private final OrderService orderService;
    private final ReportService reportService;
    private final SecurityService securityService;

    public OrderController(OrderService orderService, ReportService reportService, SecurityService securityService) {
        this.orderService = orderService;
        this.reportService = reportService;
        this.securityService = securityService;
    }

    @PostMapping(consumes = APPLICATION_JSON, produces = APPLICATION_JSON)
    @Operation(summary = "Create a new order", description = "Places a new order for a customer")
    @ApiResponse(responseCode = "201", description = "Order created successfully")
    @ApiResponse(responseCode = "400", description = "Invalid order data")
    @ApiResponse(responseCode = "404", description = "Customer or item not found")
    @ResponseStatus(HttpStatus.CREATED)
    public OrderDetailsDTO addOrder(@Valid @RequestBody CreateOrderDTO dto) {
        return orderService.addOrder(dto);
    }

    @GetMapping(value = "/{id}", produces = APPLICATION_JSON)
    @Operation(summary = "View customer order report", description = "Retrieves all orders and shipping details for a customer")
    @ApiResponse(responseCode = "200", description = "Report retrieved successfully")
    @ApiResponse(responseCode = "401", description = "Unauthorized - invalid authorization header")
    @ApiResponse(responseCode = "403", description = "Forbidden - insufficient permissions")
    @ApiResponse(responseCode = "404", description = "Customer not found")
    @ResponseStatus(HttpStatus.OK)
    public ReportDTO viewReport(@RequestHeader(value = "Authorization", required = false) String authorization, @PathVariable Long id) {
        Long userId = securityService.validateAuthorization(authorization, Feature.VIEW_REPORT);
        if (!userId.equals(id)) {
            throw new ForbiddenException();
        }
        return reportService.getReportForCustomerWithId(id);
    }

    @PostMapping(value = "/{orderId}/reorder", produces = APPLICATION_JSON)
    @Operation(summary = "Reorder items", description = "Reorders all items from a previous order")
    @ApiResponse(responseCode = "201", description = "Order created successfully")
    @ApiResponse(responseCode = "401", description = "Unauthorized - invalid authorization header")
    @ApiResponse(responseCode = "403", description = "Forbidden - insufficient permissions")
    @ApiResponse(responseCode = "404", description = "Order not found")
    @ResponseStatus(HttpStatus.CREATED)
    public OrderDetailsDTO reorder(@RequestHeader(value = "Authorization", required = false) String authorization, @PathVariable Long orderId) {
        Long userId = securityService.validateAuthorization(authorization, Feature.REORDER);
        return orderService.reorder(userId, orderId);
    }

    @GetMapping("/shipping_today")
    @Operation(summary = "Get orders shipping today")
    @ApiResponse(responseCode = "200", description = "Orders retrieved")
    @ApiResponse(responseCode = "401", description = "Unauthorized")
    @ApiResponse(responseCode = "403", description = "Forbidden")
    public List<ShippingDTO> OrdersWithItemsShippingToday(@RequestHeader(value = "Authorization", required = false) String authorization) {
        securityService.validateAuthorization(authorization, Feature.GET_ORDER_OF_ITEM_WITH_SHIPPING_DATE);
        return reportService.getOrdersWithItemsShippingToday();
    }
}
