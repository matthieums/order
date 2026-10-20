package com.switchfully.eurder.eurder.repository;

import com.switchfully.eurder.eurder.domain.Item;
import com.switchfully.eurder.eurder.exception.ItemAlreadyExistsException;
import com.switchfully.eurder.eurder.exception.ItemDoesNotExistException;
import org.springframework.stereotype.Repository;

import javax.swing.text.html.Option;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.concurrent.ConcurrentHashMap;

@Repository
public class ItemRepository {
    public Map<String, Item> availableItems;

    public ItemRepository() {
        this.availableItems = new ConcurrentHashMap<>();
    }

    public Item addItem(Item item) {
        Item existing = availableItems.putIfAbsent(item.getId(), item);

        if (existing != null) {
            throw new ItemAlreadyExistsException(item.getId());
        }

        return item;
    }

    public List<Item> addItems(List<Item> items) {
        return items.stream()
                .map(this::addItem)
                .toList();
    }

    public Item updateItem(String id, Item item) {
        availableItems.put(id, item);
        return item;
    }

    public boolean contains(Item item) {
        return availableItems.containsValue(item);
    }

    public boolean containsById(String itemId) {
        return availableItems.containsKey(itemId);
    }

    public Item getById(String id) {
        return Optional.ofNullable(availableItems.get(id))
                .orElseThrow(() -> new ItemDoesNotExistException(id));
    }

    public Optional<Integer> getStock(String itemId) {
        return Optional.ofNullable(availableItems.get(itemId))
                .map(Item::getAmount);
    }

    public void subtractFromStock(String itemId, int requiredAMount) {
        Item item = getById(itemId);

       availableItems.put(
               itemId,
               new Item(
                       itemId,
                       item.getName(),
                       item.getDescription(),
                       item.getPrice(),
                       item.getAmount() - requiredAMount
                       )
       );
    }

    public double getPrice(String itemId) {
        return availableItems.get(itemId).getPrice();
    }

    public void remove(Item item) {
        availableItems.remove(item.getId());
    }
}
