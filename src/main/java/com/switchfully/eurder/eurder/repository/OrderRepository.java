package com.switchfully.eurder.eurder.repository;

import com.switchfully.eurder.eurder.domain.Eurder;
import com.switchfully.eurder.eurder.exception.OrderAlreadyExistsException;
import com.switchfully.eurder.eurder.exception.OrderDoesNotExistException;
import org.springframework.stereotype.Repository;

import java.util.Map;
import java.util.Optional;
import java.util.concurrent.ConcurrentHashMap;

@Repository
public class OrderRepository {
    public Map<String, Eurder> orders;

    public OrderRepository() {
        this.orders = new ConcurrentHashMap<>();
    }

    public Eurder addOrder(Eurder order) {
        Eurder existing = orders.putIfAbsent(order.getId(), order);

        if (existing != null) {
            throw new OrderAlreadyExistsException(order.getId());
        }

        return order;
    }

    public Eurder getOrderById(String id) {
        return Optional.ofNullable(orders.get(id))
                .orElseThrow(() -> new OrderDoesNotExistException(id));
    }
}
