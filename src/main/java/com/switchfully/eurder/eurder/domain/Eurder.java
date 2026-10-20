package com.switchfully.eurder.eurder.domain;

import com.switchfully.eurder.eurder.repository.ItemRepository;

import java.time.LocalDate;
import java.util.List;
import java.util.UUID;

public class Eurder {
    private final String id;
    private final List<ItemGroup> itemGroups;
    private final String placed_by;
    private double totalPrice;

    public Eurder(List<ItemGroup> itemGroups, String placed_by) {
        this.id = UUID.randomUUID().toString();
        this.itemGroups = itemGroups;
        this.placed_by = placed_by;
        this.totalPrice = calculatePrice();
    }

    public String getId() {
        return id;
    }

    public double getTotalPrice() {
        return totalPrice;
    }

    public List<ItemGroup> getItemGroups() {
        return itemGroups;
    }

    public String getPlaced_by() {
        return placed_by;
    }

    private double calculatePrice() {
        return getItemGroups().stream()
                .mapToDouble(group -> group.getAmount() * group.getUnitPrice())
                .sum();
    }
}
