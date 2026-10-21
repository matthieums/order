package com.switchfully.eurder.eurder.domain.user;

import jakarta.validation.constraints.NotBlank;

public class Customer extends User {
    @NotBlank
    String firstname;
    @NotBlank
    String lastname;
    @NotBlank
    String email;
    @NotBlank
    String address;
    @NotBlank
    String phoneNumber;

    public Customer(String firstname, String lastname, String email, String password, String address, String phoneNumber) {
        super(UserType.CUSTOMER, firstname, lastname, email, password);
        this.address = address;
        this.phoneNumber = phoneNumber;
    }

    public String getAddress() {
        return address;
    }

    public String getPhoneNumber() {
        return phoneNumber;
    }
}
