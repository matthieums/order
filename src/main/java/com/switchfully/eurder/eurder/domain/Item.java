package com.switchfully.eurder.eurder.domain;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Positive;
import jakarta.validation.constraints.PositiveOrZero;

import java.util.UUID;

public class Item {
    String id;
    @NotBlank
    String name;
    @NotBlank
    String description;
    @Positive
    double price;
    @PositiveOrZero
    int amount;

    public Item(String name, String description, double price, int amount) {
        this.id = UUID.randomUUID().toString();
        this.name = name;
        this.description = description;
        this.price = price;
        this.amount = amount;
    }

    public Item(String id, String name, String description, double price, int amount) {
        this.id = id;
        this.name = name;
        this.description = description;
        this.price = price;
        this.amount = amount;
    }


    public String getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public String getDescription() {
        return description;
    }

    public double getPrice() {
        return price;
    }

    public int getAmount() {
        return amount;
    }

    public boolean hasQuantityAvailable(int quantity) {
        return amount >= quantity;
    }

    public void setAmount(int amount) {
        if (amount < 0 || getAmount() - amount < 0) {
            throw new RuntimeException();
        }
        this.amount = amount;
    }

    public boolean isAvailableInStock() {
        return amount > 0;
    }

    public void subtractFromStock(int quantity) {
        setAmount(getAmount() - quantity);
    }
}
