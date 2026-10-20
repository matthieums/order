package com.switchfully.eurder.eurder.domain;

import java.time.LocalDate;
import java.util.List;
import java.util.UUID;

public class Eurder {
    private final String id;
    private final List<ItemGroup> itemGroups;
    private final String placed_by;

    public Eurder(List<ItemGroup> itemGroups, String placed_by) {
        this.id = UUID.randomUUID().toString();
        this.itemGroups = itemGroups;
        this.placed_by = placed_by;
    }

    public ItemGroup addItemGroup(Item item, int amount) {
        ItemGroup newItemGroup = new ItemGroup(item.getId(), amount);
        itemGroups.add(newItemGroup);
        return newItemGroup;
    }

    public String getId() {
        return id;
    }

    public List<ItemGroup> getItemGroups() {
        return itemGroups;
    }

    public String getPlaced_by() {
        return placed_by;
    }
}
