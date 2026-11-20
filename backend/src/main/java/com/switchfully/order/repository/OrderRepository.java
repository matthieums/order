package com.switchfully.order.repository;

import com.switchfully.order.domain.Order;
import com.switchfully.order.domain.OrderItemCreatedEvent;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.UUID;

@Repository
public class OrderRepository extends BaseRepository<Order> {

    private final ApplicationEventPublisher eventPublisher;

    public OrderRepository(ApplicationEventPublisher eventPublisher) {
        this.eventPublisher = eventPublisher;
    }

    @Override
    public Order save(Order entity) {
        Order savedOrder = super.save(entity);
        savedOrder.getOrderItems()
                .forEach(orderItem -> eventPublisher.publishEvent(new OrderItemCreatedEvent(orderItem)));
        return savedOrder;
    }

    @Override
    public List<Order> getAll() {
        return getEntityManager()
                .createQuery("FROM Order", Order.class)
                .getResultList();
    }

    @Override
    public Order get(UUID entityId) {
        return getEntityManager()
                .createQuery("FROM Order where id = :id", Order.class)
                    .setParameter("id", entityId.toString())
                .getSingleResult();
    }

    public List<Order> getOrdersForCustomer(UUID customerId) {
        return getEntityManager()
                .createQuery("FROM Order where customerId = :id", Order.class)
                    .setParameter("id", customerId.toString())
                .getResultList();
    }
}
