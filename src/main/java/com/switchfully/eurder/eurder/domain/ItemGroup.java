package com.switchfully.eurder.eurder.domain;

import java.time.LocalDate;
import java.util.UUID;

public class ItemGroup {
    private final String itemId;
    private final int amount;
    private LocalDate shippingDate;

    public ItemGroup(String itemId, int amount) {
        this.itemId = itemId;
        this.amount = amount;
    }

    public String getItemId() {
        return itemId;
    }

    public int getAmount() {
        return amount;
    }

    public LocalDate getShippingDate() {
        return shippingDate;
    }

    public void setShippingDate(LocalDate shippingDate) {
        this.shippingDate = shippingDate;
    }
}
