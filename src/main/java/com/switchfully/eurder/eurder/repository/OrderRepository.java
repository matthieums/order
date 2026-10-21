package com.switchfully.eurder.eurder.repository;

import com.switchfully.eurder.eurder.api.dtos.ReportDTO;
import com.switchfully.eurder.eurder.domain.Eurder;
import com.switchfully.eurder.eurder.exception.OrderAlreadyExistsException;
import com.switchfully.eurder.eurder.exception.OrderDoesNotExistException;
import com.switchfully.eurder.eurder.mapper.OrderMapper;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.concurrent.ConcurrentHashMap;
import java.util.stream.Collectors;

@Repository
public class OrderRepository implements Clearable {
    public Map<String, Eurder> orders;

    public OrderRepository() {
        this.orders = new ConcurrentHashMap<>();
    }

    @Override
    public void clear() {
        orders.clear();
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

    public Map<String, Eurder> getAll() {
        return Map.copyOf(orders);
    }

    public List<Eurder> getOrdersOfCustomer(String customerMail) {
        Map<String, Eurder> allOrders = getAll();
        return allOrders.values().stream()
                .filter(order -> order.getPlaced_by().equals(customerMail))
                .toList();
    }
}
