package com.switchfully.eurder.eurder.repository;

import com.switchfully.eurder.eurder.domain.Eurder;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface OrderRepository extends JpaRepository<Eurder, Long> {
    public List<Eurder> findAllByCustomerId(Long customerId);

//    public Map<String, Eurder> orders;
//
//    public OrderRepository() {
//        this.orders = new ConcurrentHashMap<>();
//    }
//
//    @Override
//    public void clear() {
//        orders.clear();
//    }
//
//    public Eurder addOrder(Eurder order) {
//        Eurder existing = orders.putIfAbsent(order.getId(), order);
//
//        if (existing != null) {
//            throw new OrderAlreadyExistsException(order.getId());
//        }
//        return order;
//    }
//
//    public Eurder getOrderById(String id) {
//        return Optional.ofNullable(orders.get(id))
//                .orElseThrow(() -> new OrderDoesNotExistException(id));
//    }
//
//    public Map<String, Eurder> getAll() {
//        return Map.copyOf(orders);
//    }
//
//    public List<Eurder> getOrdersOfCustomer(String customerMail) {
//        Map<String, Eurder> allOrders = getAll();
//        return allOrders.values().stream()
//                .filter(order -> order.getPlaced_by().equals(customerMail))
//                .toList();
//    }
//
//    public List<Eurder> OrdersWithItemsShippingToday() {
//        List<Eurder> allOrders = getAll().values().stream().toList();
//        return allOrders.stream()
//                .filter(order ->
//                        order.hasShippingDateOf(LocalDate.now())
//                )
//                .toList();
//
//    }
}
