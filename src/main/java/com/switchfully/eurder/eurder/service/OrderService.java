package com.switchfully.eurder.eurder.service;

import com.switchfully.eurder.eurder.api.dtos.CreateOrderDTO;
import com.switchfully.eurder.eurder.api.dtos.OrderDTO;
import com.switchfully.eurder.eurder.api.dtos.ReportDTO;
import com.switchfully.eurder.eurder.domain.Eurder;
import com.switchfully.eurder.eurder.domain.Item;
import com.switchfully.eurder.eurder.domain.ItemGroup;
import com.switchfully.eurder.eurder.exception.ForbiddenException;
import com.switchfully.eurder.eurder.exception.OrderDoesNotExistException;
import com.switchfully.eurder.eurder.mapper.ItemGroupMapper;
import com.switchfully.eurder.eurder.mapper.OrderMapper;
import com.switchfully.eurder.eurder.mapper.ReportMapper;
import com.switchfully.eurder.eurder.repository.ItemRepository;
import com.switchfully.eurder.eurder.repository.OrderRepository;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;
import java.util.Map;

@Service
public class OrderService {
    private final OrderRepository orderRepository;
    private final ItemRepository itemRepository;

    public OrderService(OrderRepository orderRepository, ItemRepository itemRepository) {
        this.orderRepository = orderRepository;
        this.itemRepository = itemRepository;
    }

    private List<ItemGroup> toItemGroups(Map<String, Integer> itemIdsToAmounts) {
//        Checks if item exists and creates the group if it exists
//        You shouldn't be able to create an order of an item which does not exist in the repository
        return itemIdsToAmounts.entrySet().stream()
                .map(entry -> {
                    Item item = itemRepository.getById(entry.getKey());
                    return ItemGroupMapper.toItemGroup(item, entry.getValue());
                })
                .toList();
    }

    public OrderDTO addOrder(CreateOrderDTO dto) {
        List<ItemGroup> groups = toItemGroups(dto.itemIdsToAmounts());
        Eurder order = new Eurder(groups, dto.placedBy());
        groups.forEach(group -> {
                    boolean immediatelyAvailable = setShippingDateBasedOnStock(group);

                    if (immediatelyAvailable) {
                        subtractFromStock(group.getItemId(), group.getAmount());
                    }
                }
        );

        return OrderMapper.toDTO(orderRepository.addOrder(order));
    }

    private void subtractFromStock(String itemId, int amount) {
        itemRepository.subtractFromStock(
                itemId,
                amount
        );
    }

    private boolean setShippingDateBasedOnStock(ItemGroup group) {
        boolean available = itemRepository.getStock(group.getItemId())
                .map(stock -> stock >= group.getAmount())
                .orElse(false);

        group.setShippingDate(
                LocalDate.now().plusDays((available ? 1 : 7))
        );

        return available;
    }

    public ReportDTO getReportForCustomerWithId(String mail) {
        List<Eurder> customerOrders = orderRepository.getOrdersOfCustomer(mail);
        return ReportMapper.toDto(customerOrders);
    }

    public OrderDTO reorder(String userId, String orderId) {
        Eurder order = orderRepository.getOrderById(orderId);

        if (!order.getPlaced_by().equals(userId)) {
            throw new OrderDoesNotExistException(orderId);
        }

        return OrderMapper.toDTO(orderRepository.addOrder(
                        new Eurder(order.getItemGroups(), userId)
                )
        );
    }
}
