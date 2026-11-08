package com.switchfully.eurder.eurder.repository;

import com.switchfully.eurder.eurder.domain.Item;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;


@Repository
public interface ItemRepository extends JpaRepository<Item, Long> {
//    public Map<String, Item> availableItems;
//
//    public ItemRepository() {
//        this.availableItems = new ConcurrentHashMap<>();
//    }
//
//    @Override
//    public void clear() {
//        availableItems.clear();
//    }
//
//    public Item addItem(Item item) {
//        Item existing = availableItems.putIfAbsent(item.getId(), item);
//
//        if (existing != null) {
//            throw new ItemAlreadyExistsException(item.getId());
//        }
//
//        return item;
//    }
//
//    public List<Item> addItems(List<Item> items) {
//        return items.stream()
//                .map(this::addItem)
//                .toList();
//    }
//
//    public Item updateItem(String id, Item item) {
//        availableItems.put(id, item);
//        return item;
//    }
//
//    public boolean contains(Item item) {
//        return availableItems.containsValue(item);
//    }
//
//    public Item getById(String id) {
//        return Optional.ofNullable(availableItems.get(id))
//                .orElseThrow(() -> new ItemDoesNotExistException(id));
//    }
//

//
//
//    public double getPrice(String itemId) {
//        return availableItems.get(itemId).getPrice();
//    }
//
//    public void remove(Item item) {
//        availableItems.remove(item.getId());
//    }


}
