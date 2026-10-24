package com.switchfully.eurder.eurder.domain;

import com.switchfully.eurder.eurder.repository.ItemRepository;

import java.time.LocalDate;
import java.util.UUID;

public class ItemGroup {
    private final String itemId;
    private final String itemName;
    private final int amount;
    private LocalDate shippingDate;
    private double totalPrice;
    private double unitPrice;

    public ItemGroup(String itemId, String itemName, int amount, double unitPrice) {
        this.itemId = itemId;
        this.itemName = itemName;
        this.amount = amount;
        this.unitPrice = unitPrice;
        this.totalPrice = unitPrice * amount;
    }

    public String getItemName() {
        return itemName;
    }

    public String getItemId() {
        return itemId;
    }

    public int getAmount() {
        return amount;
    }

    public double getTotalPrice() {
        return totalPrice;
    }

    public LocalDate getShippingDate() {
        return shippingDate;
    }

    public double getUnitPrice() {
        return unitPrice;
    }

    public void setShippingDate(LocalDate shippingDate) {
        this.shippingDate = shippingDate;
    }

    public boolean hasShippingDateOf(LocalDate date) {
        return shippingDate.isEqual(date);
    }

}
