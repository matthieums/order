package com.switchfully.order.domain;

import org.springframework.context.ApplicationEvent;

/**
 * An event that is thrown when an OrderItem is created
 */
public class OrderItemCreatedEvent extends ApplicationEvent {

    private OrderItem orderItem;

    public OrderItemCreatedEvent(OrderItem orderItem) {
        super(orderItem);
        this.orderItem = orderItem;
    }

    public OrderItem getOrderItem() {
        return orderItem;
    }
}
