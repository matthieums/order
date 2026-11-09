package com.switchfully.eurder.eurder.service;

import com.switchfully.eurder.eurder.api.dtos.*;
import com.switchfully.eurder.eurder.domain.Eurder;
import com.switchfully.eurder.eurder.domain.Item;
import com.switchfully.eurder.eurder.domain.ItemGroup;
import com.switchfully.eurder.eurder.domain.user.Customer;
import com.switchfully.eurder.eurder.exception.CustomerDoesNotExistException;
import com.switchfully.eurder.eurder.exception.ItemDoesNotExistException;
import com.switchfully.eurder.eurder.exception.OrderDoesNotExistException;
import com.switchfully.eurder.eurder.mapper.ItemGroupMapper;
import com.switchfully.eurder.eurder.mapper.OrderMapper;
import com.switchfully.eurder.eurder.mapper.ReportMapper;
import com.switchfully.eurder.eurder.repository.CustomerRepository;
import com.switchfully.eurder.eurder.repository.ItemGroupRepository;
import com.switchfully.eurder.eurder.repository.ItemRepository;
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
    private final ItemRepository itemRepository;
    private final CustomerRepository customerRepository;
    private final ItemGroupRepository itemGroupRepository;
    private final OrderMapper orderMapper;
    private final ReportMapper reportMapper;
    private final ItemGroupMapper itemGroupMapper;

    public OrderService(OrderRepository orderRepository, ItemRepository itemRepository, CustomerRepository customerRepository, ItemGroupRepository itemGroupRepository, OrderMapper orderMapper, ReportMapper reportMapper, ItemGroupMapper itemGroupMapper) {
        this.orderRepository = orderRepository;
        this.itemRepository = itemRepository;
        this.customerRepository = customerRepository;
        this.itemGroupRepository = itemGroupRepository;
        this.orderMapper = orderMapper;
        this.reportMapper = reportMapper;
        this.itemGroupMapper = itemGroupMapper;
    }

    public OrderDetailsDTO addOrder(CreateOrderDTO dto) {
        Customer customer = customerRepository.findById(dto.customerId())
                .orElseThrow(() -> new CustomerDoesNotExistException(dto.customerId()));

        Eurder order = new Eurder(customer);

        List<ItemGroup> itemGroups = createItemGroups(order, dto.itemGroups());

        return saveOrder(order, itemGroups);
    }

    public List<ItemGroup> createItemGroups(Eurder order, List<CreateItemGroupDTO> DTOs) {
        return DTOs.stream()
                .map(dto -> {
                    Item item = itemRepository.findById(dto.itemId())
                            .orElseThrow(() ->
                                    new ItemDoesNotExistException(dto.itemId()));

                    return itemGroupMapper.toDomain(order, dto, item);
                })
                .toList();
    }

    private OrderDetailsDTO saveOrder(Eurder order, List<ItemGroup> itemGroups) {

        itemGroups.forEach(this::processItemGroupStock);

        Eurder savedOrder = orderRepository.save(order);

        List<ItemGroup> savedItemGroups =
        itemGroupRepository.saveAll(itemGroups);

        BigDecimal totalPrice = calculateTotalOrderPrice(savedItemGroups);

        return orderMapper.toDetailsDTO(savedOrder, savedItemGroups, totalPrice);
    }

    private void processItemGroupStock(ItemGroup group) {
        boolean immediatelyAvailable = setShippingDateBasedOnStock(group);

        if (immediatelyAvailable) {
            subtractFromStock(group.getItem(), group.getAmount());
        }
    }

    private BigDecimal calculateTotalOrderPrice(List<ItemGroup> itemGroups) {
        return itemGroups.stream()
                .map(ItemGroup::calculatePrice)
                .reduce(BigDecimal.ZERO, BigDecimal::add);
    }

    private void subtractFromStock(Item item, int requiredAmount) {
        item.subtractAmount(requiredAmount);
    }

    private boolean setShippingDateBasedOnStock(ItemGroup group) {
        boolean available = group.getItem().getAmount() >= group.getAmount();

        group.setShippingDate(
                LocalDate.now().plusDays((available ? 1 : 7))
        );
        return available;
    }

    public OrderDetailsDTO reorder(Long userId, Long orderId) {
        Eurder order = orderRepository.findById(orderId)
                .orElseThrow(() -> new OrderDoesNotExistException(orderId));

        if (!Objects.equals(order.getCustomer().getId(), userId)) {
            throw new OrderDoesNotExistException(orderId);
        }

        List<ItemGroup> originalGroups = itemGroupRepository.findAllByEurder_Id(orderId);

        Eurder newOrder = order.duplicate();

        List<ItemGroup> newGroups = originalGroups.stream()
                .map(group -> group.duplicateWithUpdatedItemPrice(newOrder))
                .toList();

        return saveOrder(newOrder, newGroups);
    }

    public ReportDTO getReportForCustomerWithId(Long id) {
        Customer customer = customerRepository.findById(id)
                .orElseThrow(() -> new CustomerDoesNotExistException(id));

        List<Eurder> orders = orderRepository.findAllByCustomerId(id);

        List<OrderReportDTO> orderReportsDTOs = orders.stream()
                .map(order -> {
                    List<ItemGroup> itemGroups =
                            itemGroupRepository.findAllByEurder_Id(order.getId());
                    return reportMapper.toOrderReportDTO(order.getId(), itemGroups);
                })
                .toList();

        return reportMapper.toDto(customer, orderReportsDTOs);
    }

    public List<ShippingDTO> getOrdersWithItemsShippingToday() {
        List<ItemGroup> itemGroupsShippingToday =
                itemGroupRepository.findAllByShippingDate(LocalDate.now());

        Map<Long, List<ItemGroup>> groupsByOrderId = itemGroupsShippingToday.stream()
                .collect(Collectors.groupingBy(
                        group -> group.getEurder().getId()
                ));

        return groupsByOrderId.values().stream()
                .map(orderMapper::toShippingDTO)
                .toList();
    }
}
