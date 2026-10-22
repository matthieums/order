package com.switchfully.eurder.eurder.api;

import com.switchfully.eurder.eurder.api.dtos.*;
import com.switchfully.eurder.eurder.domain.user.Feature;
import com.switchfully.eurder.eurder.service.ItemService;
import com.switchfully.eurder.eurder.service.OrderService;
import com.switchfully.eurder.eurder.service.SecurityService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping(value = "orders")
public class OrderController {
        private static final String APPLICATION_JSON = "application/json";
        private final OrderService orderService;
        private final SecurityService securityService;

        public OrderController(OrderService orderService, SecurityService securityService) {
            this.orderService = orderService;
                this.securityService = securityService;
        }

        @PostMapping(consumes = APPLICATION_JSON, produces = APPLICATION_JSON)
        @ResponseStatus(HttpStatus.CREATED)
        public OrderDTO addOrder(@Valid @RequestBody CreateOrderDTO dto) {
                return orderService.addOrder(dto);
        }

        @GetMapping(value = "/{id}",  produces = APPLICATION_JSON)
        @ResponseStatus(HttpStatus.OK)
        public ReportDTO viewReport(@PathVariable String id) {
                return orderService.getReportForCustomerWithId(id);
        }

        @PostMapping(value = "{orderId}/reorder", consumes = APPLICATION_JSON, produces = APPLICATION_JSON)
        @ResponseStatus(HttpStatus.CREATED)
        public OrderDTO reorder(@RequestHeader(value = "Authorization", required = false) String authorization, @PathVariable String orderId) {
                String userId = securityService.validateAuthorization(authorization, Feature.REORDER);
                return orderService.reorder(userId, orderId);
        }

}
