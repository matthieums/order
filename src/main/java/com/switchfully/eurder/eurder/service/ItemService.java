package com.switchfully.eurder.eurder.service;

import com.switchfully.eurder.eurder.api.dtos.CreateItemDTO;
import com.switchfully.eurder.eurder.api.dtos.ItemDTO;
import com.switchfully.eurder.eurder.domain.Item;
import com.switchfully.eurder.eurder.exception.ItemDoesNotExistException;
import com.switchfully.eurder.eurder.mapper.ItemMapper;
import com.switchfully.eurder.eurder.repository.ItemRepository;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
public class ItemService {
    private final ItemRepository itemRepository;
    private final ItemMapper itemMapper;

    public ItemService(ItemRepository itemRepository, ItemMapper itemMapper) {
        this.itemRepository = itemRepository;
        this.itemMapper = itemMapper;
    }

    public ItemDTO addItem(CreateItemDTO dto) {
        Item item = itemRepository.save(itemMapper.toObject(dto));
        return itemMapper.toDTO(item);
    }

    public ItemDTO updateItem(Long itemId, CreateItemDTO dto) {
        Item item = itemRepository.findById(itemId)
                .orElseThrow(() -> new ItemDoesNotExistException(itemId));

        item.setName(dto.name());
        item.setPrice(dto.price());
        item.setDescription(dto.description());
        item.setAmount(dto.amount());

        Item updatedItem = itemRepository.save(item);

        return itemMapper.toDTO(updatedItem);
    }
}
