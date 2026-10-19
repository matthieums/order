package com.switchfully.eurder.eurder.api.dtos;

public class CreateCustomerDTO {
    private final String firstname;
    private final String lastname;
    private final String email;
    private final String address;
    private final String phoneNumber;

    public CreateCustomerDTO(String firstname, String lastname, String email, String address, String phoneNumber) {
        this.firstname = firstname;
        this.lastname = lastname;
        this.email = email;
        this.address = address;
        this.phoneNumber = phoneNumber;
    }
}
