package com.switchfully.eurder.eurder.domain;

import java.math.BigDecimal;

public class ItemFactory {
    public static Item itemWithInfiniteSupply() {
        return new Item(
                "Item",
                "An item",
                new BigDecimal("1.00"),
                999
        );
    }

    public static Item itemWithInfiniteSupply(Long id) {
        Item item = new Item(
                "Item",
                "An item",
                new BigDecimal("1.00"),
                999
        );
        item.id = id;
        return item;
    }

    public static Item itemWithAmount(int amount) {
        return new Item(
                "Item",
                "An item",
                new BigDecimal("1.00"),
                amount
        );
    }
}
