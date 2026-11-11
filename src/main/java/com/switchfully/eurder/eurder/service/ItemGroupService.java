package com.switchfully.eurder.eurder.service;

import com.switchfully.eurder.eurder.api.dtos.CreateItemGroupDTO;
import com.switchfully.eurder.eurder.domain.Eurder;
import com.switchfully.eurder.eurder.domain.Item;
import com.switchfully.eurder.eurder.domain.ItemGroup;
import com.switchfully.eurder.eurder.mapper.ItemGroupMapper;
import com.switchfully.eurder.eurder.repository.ItemGroupRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;

@Service
@Transactional
public class ItemGroupService {
    private final ItemGroupRepository itemGroupRepository;
    private final ItemService itemService;
    private final ItemGroupMapper itemGroupMapper;

    public ItemGroupService(ItemGroupRepository itemGroupRepository, ItemService itemService, ItemGroupMapper itemGroupMapper) {
        this.itemGroupRepository = itemGroupRepository;
        this.itemService = itemService;
        this.itemGroupMapper = itemGroupMapper;
    }

    public List<ItemGroup> saveAll(List<ItemGroup> groups) {
        return itemGroupRepository.saveAll(groups);
    }

    public List<ItemGroup> findAllByEurder_Id(Long id) {
        return itemGroupRepository.findAllByEurder_Id(id);
    }

    public List<ItemGroup> findAllByShippingDate(LocalDate date) {
        return itemGroupRepository.findAllByShippingDate(date);
    }

    public BigDecimal calculateTotalPriceOfGroups(List<ItemGroup> itemGroups) {
        return itemGroups.stream()
                .map(ItemGroup::calculatePrice)
                .reduce(BigDecimal.ZERO, BigDecimal::add);
    }

    public List<ItemGroup> createItemGroups(Eurder order, List<CreateItemGroupDTO> dtos) {
        return dtos.stream()
                .map(dto -> {
                    Item item = itemService.findById(dto.itemId());
                    return itemGroupMapper.toDomain(order, dto, item);
                })
                .toList();
    }

    public List<ItemGroup> duplicateWithUpdatedPrices(Eurder original, Eurder newOrder) {
        List<ItemGroup> originalGroups = findAllByEurder_Id(original.getId());
        return originalGroups.stream()
                .map(group -> group.duplicateWithUpdatedItemPrice(newOrder))
                .toList();
    }
}
