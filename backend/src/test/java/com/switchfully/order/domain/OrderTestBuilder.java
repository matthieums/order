package com.switchfully.order.domain;

import com.switchfully.order.domain.Order.OrderBuilder;

import java.util.Arrays;
import java.util.UUID;

import static com.switchfully.order.domain.OrderItemTestBuilder.anOrderItem;

public class OrderTestBuilder {

    private OrderBuilder orderBuilder;

    private OrderTestBuilder(OrderBuilder orderBuilder) {
        this.orderBuilder = orderBuilder;
    }

    public static OrderTestBuilder anEmptyOrder() {
        return new OrderTestBuilder(OrderBuilder.order());
    }

    public static OrderTestBuilder anOrder() {
        return new OrderTestBuilder(OrderBuilder.order()
        .withCustomerId(UUID.randomUUID())
        .withOrderItems(Arrays.asList(anOrderItem().build(), anOrderItem().build())));
    }

        public Order build() {
        return new Order(orderBuilder);
    }

    public OrderTestBuilder withId(UUID id) {
        orderBuilder.withId(id);
        return this;
    }

    public OrderTestBuilder withOrderItems(OrderItem... orderItems) {
        orderBuilder.withOrderItems(Arrays.asList(orderItems));
        return this;
    }

    public OrderTestBuilder withCustomerId(UUID customerId) {
        orderBuilder.withCustomerId(customerId);
        return this;
    }
}