package com.switchfully.order.service;

import com.switchfully.order.domain.Order;
import com.switchfully.order.domain.OrderItem;
import org.springframework.stereotype.Component;

import java.util.List;

/**
 * Instead of our custom validator(s),
 * we could also use Jakarta Validation (we didn't use it here, to not over-complexify the solution).
 * That way, we can indicate on our Domain classes (such as Customer and Item)
 * what fields are required and what the valid values are for each field.
 */
@Component
public class OrderValidator extends EntityValidator<Order> {

    @Override
    protected boolean isAFieldEmptyOrNull(Order order) {
        return isNull(order)
                || isNull(order.getCustomerId())
                || order.getOrderItems().size() < 1
                || isAnOrderItemInvalid(order.getOrderItems());
    }

    private boolean isAnOrderItemInvalid(List<OrderItem> orderItems) {
        return orderItems.stream()
                .map(orderItem ->
                        isNull(orderItem.getItemId())
                                || isNull(orderItem.getItemPrice())
                                || orderItem.getItemPrice().getAmount().doubleValue() <= 0
                                || orderItem.getOrderedAmount() <= 0
                                || isNull(orderItem.getShippingDate()))
                .filter(isInvalidOrderItem -> isInvalidOrderItem)
                .findFirst()
                .orElse(false);
    }

}
