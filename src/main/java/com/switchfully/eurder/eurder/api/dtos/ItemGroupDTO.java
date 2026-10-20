package com.switchfully.eurder.eurder.api.dtos;

public class ItemGroupDTO {
    private final String itemName;
    private final int orderedAmount;
    private final double totalPrice;

    public ItemGroupDTO(String itemName, int orderedAmount, double totalPrice) {
        this.itemName = itemName;
        this.orderedAmount = orderedAmount;
        this.totalPrice = totalPrice;
    }

    public String getItemName() {
        return itemName;
    }

    public int getOrderedAmount() {
        return orderedAmount;
    }

    public double getTotalPrice() {
        return totalPrice;
    }
}
