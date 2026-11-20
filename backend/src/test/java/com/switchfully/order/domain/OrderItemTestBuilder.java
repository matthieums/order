package com.switchfully.order.domain;

import com.switchfully.order.domain.OrderItem.OrderItemBuilder;

import java.math.BigDecimal;
import java.util.UUID;

public class OrderItemTestBuilder{

    private OrderItemBuilder orderItemBuilder;

    private OrderItemTestBuilder(OrderItemBuilder orderItemBuilder) {
        this.orderItemBuilder = orderItemBuilder;
    }

    public static OrderItemTestBuilder anEmptyOrderItem() {
        return new OrderItemTestBuilder(OrderItem.OrderItemBuilder.orderItem());
    }

    public static OrderItemTestBuilder anOrderItem() {
        return new OrderItemTestBuilder(OrderItem.OrderItemBuilder.orderItem()
            .withItemId(UUID.randomUUID())
            .withItemPrice(Price.create(BigDecimal.valueOf(49.95)))
            .withOrderedAmount(10)
            .withShippingDateBasedOnAvailableItemStock(15)
        );
    }

        public OrderItem build() {
        return orderItemBuilder.build();
    }

    public OrderItemTestBuilder withItemId(UUID itemId) {
        orderItemBuilder.withItemId(itemId);
        return this;
    }

    public OrderItemTestBuilder withItemPrice(Price itemPrice) {
        orderItemBuilder.withItemPrice(itemPrice);
        return this;
    }

    public OrderItemTestBuilder withOrderedAmount(int orderedAmount) {
        orderItemBuilder.withOrderedAmount(orderedAmount);
        return this;
    }

    public OrderItemTestBuilder withShippingDateBasedOnAvailableItemStock(int availableItemStock) {
        orderItemBuilder.withShippingDateBasedOnAvailableItemStock(availableItemStock);
        return this;
    }
}