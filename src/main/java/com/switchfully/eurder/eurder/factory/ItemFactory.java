package com.switchfully.eurder.eurder.factory;

import com.switchfully.eurder.eurder.api.dtos.CreateItemDTO;
import com.switchfully.eurder.eurder.domain.Item;

public class ItemFactory {
    public static Item itemWithInfiniteSupply() {
        return new Item(
                "Item",
                "An item",
                1.0d,
                999
        );
    }
}
