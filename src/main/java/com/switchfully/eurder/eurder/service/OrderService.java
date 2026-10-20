package com.switchfully.eurder.eurder.service;

import com.switchfully.eurder.eurder.api.dtos.CreateOrderDTO;
import com.switchfully.eurder.eurder.api.dtos.OrderDTO;
import com.switchfully.eurder.eurder.api.dtos.ReportDTO;
import com.switchfully.eurder.eurder.domain.Eurder;
import com.switchfully.eurder.eurder.domain.Item;
import com.switchfully.eurder.eurder.domain.ItemGroup;
import com.switchfully.eurder.eurder.mapper.OrderMapper;
import com.switchfully.eurder.eurder.mapper.ReportMapper;
import com.switchfully.eurder.eurder.repository.ItemRepository;
import com.switchfully.eurder.eurder.repository.OrderRepository;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;

@Service
public class OrderService {
    private final OrderRepository orderRepository;
    private final ItemRepository itemRepository;

    public OrderService(OrderRepository orderRepository, ItemRepository itemRepository) {
        this.orderRepository = orderRepository;
        this.itemRepository = itemRepository;
    }

    public OrderDTO addOrder(CreateOrderDTO dto) {
        List<ItemGroup> groups = dto.itemIdsToAmounts().entrySet().stream()
                .map(group -> {
                    Item item = itemRepository.getById(group.getKey());
                    return new ItemGroup(
                            item.getId(),
                            item.getName(),
                            group.getValue(),
                            item.getPrice()
                    );
                })
                .toList();

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
        // Check if there's enough stock for this item group
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
}
