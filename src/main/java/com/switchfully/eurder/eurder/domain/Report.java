package com.switchfully.eurder.eurder.domain;

import java.util.List;

public record Report(String orderID, List<ItemGroup> itemGroups, double totalPrice) {
}


