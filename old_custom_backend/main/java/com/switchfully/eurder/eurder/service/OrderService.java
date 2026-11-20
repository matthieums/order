package com.switchfully.eurder.eurder.service;

import com.switchfully.eurder.eurder.api.dtos.*;
import com.switchfully.eurder.eurder.domain.Eurder;
import com.switchfully.eurder.eurder.domain.ItemGroup;
import com.switchfully.eurder.eurder.domain.user.Customer;
import com.switchfully.eurder.eurder.exception.OrderDoesNotExistException;
import com.switchfully.eurder.eurder.mapper.OrderMapper;
import com.switchfully.eurder.eurder.mapper.ReportMapper;
import com.switchfully.eurder.eurder.repository.OrderRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.stream.Collectors;

@Service
@Transactional
public class OrderService {
    private final OrderRepository orderRepository;

    private final CustomerService customerService;
    private final StockService stockService;
    private final ItemGroupService itemGroupService;

    private final OrderMapper orderMapper;

    public OrderService(
            OrderRepository orderRepository,
            CustomerService customerService,
            OrderMapper orderMapper,
            StockService stockService,
            ItemGroupService itemGroupService
    ) {
        this.orderRepository = orderRepository;
        this.customerService = customerService;
        this.orderMapper = orderMapper;
        this.stockService = stockService;
        this.itemGroupService = itemGroupService;
    }

    public OrderDetailsDTO addOrder(CreateOrderDTO dto) {
        Customer customer = customerService.findById(dto.customerId());
        Eurder order = new Eurder(customer);
        List<ItemGroup> itemGroups = itemGroupService.createItemGroups(order, dto.itemGroups());
        return saveOrder(order, itemGroups);
    }

    private OrderDetailsDTO saveOrder(Eurder order, List<ItemGroup> itemGroups) {
        itemGroups.forEach(stockService::processItemGroupStock);

        Eurder savedOrder = orderRepository.save(order);
        List<ItemGroup> savedItemGroups = itemGroupService.saveAll(itemGroups);
        BigDecimal totalPrice = itemGroupService.calculateTotalPriceOfGroups(savedItemGroups);

        return orderMapper.toDetailsDTO(savedOrder, savedItemGroups, totalPrice);
    }

    public OrderDetailsDTO reorder(Long userId, Long orderId) {
        Eurder originalOrder = findById(orderId);

//        Order owner must correspond to the customer who reorders
        if (!Objects.equals(originalOrder.getCustomer().getId(), userId)) {
            throw new OrderDoesNotExistException(orderId);
        }

        Eurder newOrder = originalOrder.duplicate();
        List<ItemGroup> newGroups = itemGroupService.duplicateWithUpdatedPrices(originalOrder, newOrder);
        return saveOrder(newOrder, newGroups);
    }

    private Eurder findById(Long id) {
        return orderRepository.findById(id)
                .orElseThrow(() -> new OrderDoesNotExistException(id));
    }

    public List<Eurder> findAllByCustomerId(Long id) {
        return orderRepository.findAllByCustomerId(id);
    }
}
