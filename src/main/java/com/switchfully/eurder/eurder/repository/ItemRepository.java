package com.switchfully.eurder.eurder.repository;

import com.switchfully.eurder.eurder.domain.Item;
import com.switchfully.eurder.eurder.exception.ItemAlreadyExistsException;
import org.springframework.stereotype.Repository;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

@Repository
public class ItemRepository {
    public Map<String, Item> availableItems;

    public ItemRepository() {
        this.availableItems = new ConcurrentHashMap<>();
    }

    public Item addItem(Item item) {
        Item existing = availableItems.putIfAbsent(item.name(), item);

        if (existing != null) {
            throw new ItemAlreadyExistsException(item.name());
        }

        return item;
    }


    public boolean contains(Item item) {
        return availableItems.containsValue(item);
    }
}
