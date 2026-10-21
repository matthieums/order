package com.switchfully.eurder.eurder.factory;

import com.switchfully.eurder.eurder.api.dtos.CreateCustomerDTO;

public class CreateCustomerDTOFactory {
    public static CreateCustomerDTO validCreateCustomerDTO() {
        return new CreateCustomerDTO(
                "newUser",
                "user",
                MailFactory.generateRandomMail(),
                "userAddress - 5555",
                "04 555 555 555",
                "pass"
                );
    }
}
