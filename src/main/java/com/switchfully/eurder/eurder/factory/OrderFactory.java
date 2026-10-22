package com.switchfully.eurder.eurder.factory;

import com.switchfully.eurder.eurder.domain.Eurder;
import com.switchfully.eurder.eurder.domain.Item;
import com.switchfully.eurder.eurder.domain.ItemGroup;
import com.switchfully.eurder.eurder.mapper.ItemGroupMapper;

public class OrderFactory {

    public static Eurder order(Item item, int amount, String userId) {
        ItemGroup itemgroup =  ItemGroupMapper.toItemGroup(item, amount);
        return new Eurder(itemgroup, userId);
    }
}
