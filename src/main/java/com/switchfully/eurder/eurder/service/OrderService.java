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

    public List<ItemGroup> createItemGroups(List<CreateItemGroupDTO> DTOs) {
        return DTOs.stream()
                .map(dto -> {
                    Item item = itemRepository.findById(dto.itemId())
                            .orElseThrow(() -> new ItemDoesNotExistException(dto.itemId()));
                    Eurder order = orderRepository.findById(dto.orderId())
                            .orElseThrow(() -> new OrderDoesNotExistException(dto.orderId()));
                    return itemGroupMapper.toDomain(order, dto, item);
                })
                .toList();
    }

    public OrderDetailsDTO addOrder(CreateOrderDTO dto) {
        Customer customer = customerRepository.findById(dto.customerId())
                .orElseThrow(() -> new CustomerDoesNotExistException(dto.customerId()));

        List<ItemGroup> itemGroups = createItemGroups(dto.itemGroups());
        return saveOrder(new Eurder(customer), itemGroups);
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

    private OrderDetailsDTO saveOrder(Eurder order, List<ItemGroup> itemGroups) {

        itemGroups.forEach(group -> {
            boolean immediatelyAvailable = setShippingDateBasedOnStock(group);

            if (immediatelyAvailable) {
                subtractFromStock(group.getItem().getId(), group.getAmount());
            }
        });

        Eurder savedOrder = orderRepository.save(order);

        List<ItemGroup> savedItemGroups =
                itemGroupRepository.saveAll(itemGroups);

        BigDecimal totalPrice = calculateTotalOrderPrice(savedItemGroups);

    //        The three pieces of information that should be given by the DTO are combined here:
    //        -> The order info, the itemGroups, and the total price of the order
        return orderMapper.toDetailsDTO(savedOrder, savedItemGroups, totalPrice);
    }

    private BigDecimal calculateTotalOrderPrice(List<ItemGroup> itemGroups) {
        return itemGroups.stream()
                .map(ItemGroup::calculatePrice)
                .reduce(BigDecimal.ZERO, BigDecimal::add);
    }

    private void subtractFromStock(Long itemId, int requiredAmount) {
        itemRepository.findById(itemId)
                .orElseThrow(() -> new ItemDoesNotExistException(itemId))
                .subtractAmount(requiredAmount);
    }

    private boolean setShippingDateBasedOnStock(ItemGroup group) {
        boolean available = group.getItem().getAmount() > 0;

        group.setShippingDate(
                LocalDate.now().plusDays((available ? 1 : 7))
        );

        return available;
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
