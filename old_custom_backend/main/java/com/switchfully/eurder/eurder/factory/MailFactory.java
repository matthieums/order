package com.switchfully.eurder.eurder.factory;

import java.util.UUID;

public class MailFactory {
    public static String generateRandomMail() {
        return "user" + UUID.randomUUID() + "@dto.com";
    }
}
