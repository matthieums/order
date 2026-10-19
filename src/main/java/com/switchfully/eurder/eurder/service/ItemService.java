package com.switchfully.eurder.eurder.service;

import com.switchfully.eurder.eurder.api.dtos.CreateItemDTO;
import com.switchfully.eurder.eurder.api.dtos.ItemDTO;
import com.switchfully.eurder.eurder.domain.Item;
import com.switchfully.eurder.eurder.mapper.ItemMapper;
import com.switchfully.eurder.eurder.repository.ItemRepository;
import org.springframework.stereotype.Component;

@Component
public class ItemService {
    private final ItemRepository itemRepository;

    public ItemService(ItemRepository itemRepository) {
        this.itemRepository = itemRepository;
    }

    public ItemDTO addItem(CreateItemDTO dto) {
        Item item = itemRepository.addItem(ItemMapper.toObject(dto));
        return ItemMapper.toDTO(item);
    }
}
