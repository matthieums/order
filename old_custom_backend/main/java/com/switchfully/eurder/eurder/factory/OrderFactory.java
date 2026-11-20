package com.switchfully.eurder.eurder.factory;

import com.switchfully.eurder.eurder.domain.Eurder;
import com.switchfully.eurder.eurder.domain.Item;
import com.switchfully.eurder.eurder.domain.ItemGroup;
import com.switchfully.eurder.eurder.domain.user.Customer;
import com.switchfully.eurder.eurder.domain.user.User;
import com.switchfully.eurder.eurder.mapper.ItemGroupMapper;

public class OrderFactory {

    public static Eurder order() {
        return new Eurder(
                UserFactory.customerWithValidDetails()
        );
    }
    public static Eurder order(Customer customer) {
        return new Eurder(
                customer
        );
    }
}
