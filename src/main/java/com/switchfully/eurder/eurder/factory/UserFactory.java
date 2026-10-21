package com.switchfully.eurder.eurder.factory;

import com.switchfully.eurder.eurder.domain.user.Admin;
import com.switchfully.eurder.eurder.domain.user.Customer;

public class UserFactory {

    public static Customer customerWithValidDetails() {
        return new Customer(
                "newUser",
                "user",
                MailFactory.generateRandomMail(),
                "pass",
                "userAddress - 5555",
                "04 555 555 555"
        );
    }

    public static Admin admin() {
        return new Admin(
                "admin",
                "admin",
                MailFactory.generateRandomMail(),
                "admin"
        );
    }
}
