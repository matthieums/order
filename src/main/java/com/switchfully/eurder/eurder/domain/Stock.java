package com.switchfully.eurder.eurder.domain;

public class Stock {
    private Item item;
    private int quantity;

    public Stock(Item item, int quantity) {
        this.item = item;
        this.quantity = quantity;
    }

    public Item getItem() {
        return item;
    }

    public int getQuantity() {
        return quantity;
    }

    public void setQuantity(int quantity) {
        this.quantity = quantity;
    }

    public boolean reduceStock(int amount) {
        if (amount < 0 || quantity - amount < 0) {
            return false;
        }

        setQuantity(quantity - amount);
        return true;
    }
}
