package com.switchfully.eurder.eurder.factory;

import com.switchfully.eurder.eurder.api.dtos.CreateItemDTO;
import com.switchfully.eurder.eurder.domain.Item;

import java.math.BigDecimal;
import java.util.List;
import java.util.stream.IntStream;

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
        item.id = 
    }
//
//    public static Item itemWithAmount(int amount) {
//        return new Item(
//                "Item",
//                "An item",
//                1.0d,
//                amount
//        );
//    }
//
//    public static List<Item> listOfItemsWithInfSupply(int count) {
//        return IntStream.range(0, count)
//                .mapToObj(i -> itemWithInfiniteSupply())
//                .toList();
//    }
//
//    public static List<Item> listOfItemsOfAmountOne(int count) {
//        return IntStream.range(0, count)
//                .mapToObj(i -> itemWithAmount(1))
//                .toList();
//    }
}
