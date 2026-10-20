package com.switchfully.eurder.eurder.api;

import com.switchfully.eurder.eurder.api.dtos.*;
import com.switchfully.eurder.eurder.service.ItemService;
import com.switchfully.eurder.eurder.service.OrderService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping(value = "orders")
public class OrderController {
        private static final String APPLICATION_JSON = "application/json";
        private final OrderService orderService;

        public OrderController(OrderService orderService) {
            this.orderService = orderService;
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

}
