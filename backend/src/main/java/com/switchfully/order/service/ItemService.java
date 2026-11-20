package com.switchfully.order.service;

import com.switchfully.order.domain.Item;
import com.switchfully.order.repository.ItemRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.UUID;

@Service
public class ItemService {

    private final ItemRepository itemRepository;
    private final ItemValidator itemValidator;

    public ItemService(ItemRepository itemRepository, ItemValidator itemValidator) {
        this.itemRepository = itemRepository;
        this.itemValidator = itemValidator;
    }

    @Transactional(propagation = Propagation.REQUIRED)
    public Item createItem(Item item) {
        if (!itemValidator.isValidForCreation(item)) {
            itemValidator.throwInvalidStateException(item, "creation");
        }
        return itemRepository.save(item);
    }

    @Transactional(propagation = Propagation.REQUIRED)
    public Item updateItem(Item item) {
        if (!itemValidator.isValidForUpdating(item)) {
            itemValidator.throwInvalidStateException(item, "updating");
        }
        return itemRepository.update(item);
    }

    @Transactional(propagation = Propagation.SUPPORTS)
    public Item getItem(UUID itemId) {
        return itemRepository.get(itemId);
    }

    @Transactional(propagation = Propagation.REQUIRED)
    public void decrementStockForItem(UUID itemId, int amountToDecrement) {
        Item item = itemRepository.get(itemId);
        item.decrementStock(amountToDecrement);
        itemRepository.update(item);
    }

    @Transactional(propagation = Propagation.SUPPORTS)
    public List<Item> getAllItems() {
        return itemRepository.getAll();
    }
}
